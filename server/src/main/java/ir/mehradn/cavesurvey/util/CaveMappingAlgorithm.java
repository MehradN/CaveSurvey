package ir.mehradn.cavesurvey.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MaterialColor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

public class CaveMappingAlgorithm {
    public static PixelMatrix run(BlockPos startPos, int updateRadius, Level level) {
        PixelMatrix matrix = new PixelMatrix(updateRadius, startPos.getX(), startPos.getZ());
        Queue<BlockPos> updates = new ArrayDeque<>();
        updates.add(startPos);

        while (!updates.isEmpty()) {
            BlockPos pos = updates.element();
            updates.remove();

            if (matrix.outOfRange(pos) || matrix.get(pos) != null)
                continue;

            PixelInfo info = update(pos, updates, level);
            matrix.set(pos, info);
        }

        return matrix;
    }

    private static PixelInfo update(BlockPos pos, Queue<BlockPos> updates, Level level) {
        ColorGrabber colorGrabber = new ColorGrabber(level, pos);
        CloudCalculations cloud = new CloudCalculations(pos, colorGrabber);

        if (cloud.north != null)
            updates.add(cloud.north);
        if (cloud.south != null)
            updates.add(cloud.south);
        if (cloud.west != null)
            updates.add(cloud.west);
        if (cloud.east != null)
            updates.add(cloud.east);

        return cloud.pixelInfo;
    }

    public record PixelInfo(MaterialColor color, int y, int fluidDepth, boolean reachesSky) { }

    public static final class PixelMatrix {
        public PixelInfo[][] matrix;
        public final int size;
        public final int centerX;
        public final int centerZ;

        public PixelMatrix(int halfSize, int centerX, int centerZ) {
            this.size = halfSize;
            this.centerX = centerX;
            this.centerZ = centerZ;
            this.matrix = new PixelInfo[halfSize * 2][halfSize * 2];
        }

        public boolean outOfRange(int x, int z) {
            return this.centerX - this.size > x || x >= this.centerX + this.size
                || this.centerZ - this.size > z || z >= this.centerZ + this.size;
        }

        public boolean outOfRange(BlockPos pos) {
            return outOfRange(pos.getX(), pos.getZ());
        }

        public PixelInfo get(int x, int z) {
            if (outOfRange(x, z))
                return null;
            return this.matrix[x - this.centerX + this.size][z - this.centerZ + this.size];
        }

        public PixelInfo get(BlockPos pos) {
            return get(pos.getX(), pos.getZ());
        }

        public void set(BlockPos pos, PixelInfo info) {
            if (outOfRange(pos))
                return;
            this.matrix[pos.getX() - this.centerX + this.size][pos.getZ() - this.centerZ + this.size] = info;
        }
    }

    public static final class ColorGrabber {
        public final Level level;
        private final ArrayList<LevelChunk> chunks;

        public ColorGrabber(Level level, BlockPos pos) {
            this.level = level;

            ChunkPos chunkPos = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
            this.chunks = new ArrayList<>(Collections.singletonList(level.getChunk(chunkPos.x, chunkPos.z)));

            int innerX = pos.getX() & 15, innerZ = pos.getZ() & 15;
            if (innerX == 0)
                this.chunks.add(level.getChunk(chunkPos.x - 1, chunkPos.z));
            if (innerX == 15)
                this.chunks.add(level.getChunk(chunkPos.x + 1, chunkPos.z));
            if (innerZ == 0)
                this.chunks.add(level.getChunk(chunkPos.x, chunkPos.z - 1));
            if (innerZ == 15)
                this.chunks.add(level.getChunk(chunkPos.x, chunkPos.z + 1));
        }

        public MaterialColor getColor(BlockPos pos) {
            if (pos.getY() <= this.level.getMinBuildHeight() + 1)
                return Blocks.BEDROCK.defaultBlockState().getMapColor(this.level, pos);

            ChunkPos chunkPos = new ChunkPos(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
            for (LevelChunk chunk : this.chunks)
                if (chunk.getPos().equals(chunkPos))
                    return chunk.getBlockState(pos).getMapColor(this.level, pos);
            return null;
        }

        public PixelInfo getPixelInfo(BlockPos pos, int ceiling) {
            boolean reachesSky = (ceiling == this.level.getMaxBuildHeight());
            if (pos.getY() <= this.level.getMinBuildHeight() + 1) {
                return new PixelInfo(
                    Blocks.BEDROCK.defaultBlockState().getMapColor(this.level, pos),
                    pos.getY(), 0, reachesSky
                );
            }

            ChunkPos chunkPos = new ChunkPos(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
            for (LevelChunk chunk : this.chunks) {
                if (chunk.getPos().equals(chunkPos)) {
                    BlockState state = chunk.getBlockState(pos);

                    MaterialColor color;
                    int fluidDepth = 0;
                    if (state.getFluidState().isEmpty()) {
                        color = state.getMapColor(this.level, pos);
                    } else {
                        color = getCorrectStateForFluidBlock(this.level, state, pos).getMapColor(this.level, pos);
                        BlockPos fluidPos = pos;
                        while (fluidPos.getY() > this.level.getMinBuildHeight() && !chunk.getBlockState(fluidPos).getFluidState().isEmpty()) {
                            fluidPos = fluidPos.below();
                            fluidDepth++;
                        }
                    }

                    return new PixelInfo(color, pos.getY(), fluidDepth, reachesSky);
                }
            }
            return null;
        }

        private static BlockState getCorrectStateForFluidBlock(Level level, BlockState state, BlockPos pos) {
            FluidState fluidState = state.getFluidState();
            if (!fluidState.isEmpty() && !state.isFaceSturdy(level, pos, Direction.UP)) {
                return fluidState.createLegacyBlock();
            }
            return state;
        }
    }

    public static final class CloudCalculations {
        public BlockPos north = null;
        public BlockPos south = null;
        public BlockPos west = null;
        public BlockPos east = null;
        public PixelInfo pixelInfo = null;

        public CloudCalculations(BlockPos initialPosition, ColorGrabber colorGrabber) {
            if (initialPosition == null)
                return;
            BlockPos pos = new BlockPos(initialPosition);

            while (pos.getY() < colorGrabber.level.getMaxBuildHeight() && colorless(pos.above(), colorGrabber))
                pos = pos.above();
            int ceiling = pos.getY();

            do {
                if (this.north == null && colorless(pos.north(), colorGrabber))
                    this.north = pos.north();
                if (this.south == null && colorless(pos.south(), colorGrabber))
                    this.south = pos.south();
                if (this.west == null && colorless(pos.west(), colorGrabber))
                    this.west = pos.west();
                if (this.east == null && colorless(pos.east(), colorGrabber))
                    this.east = pos.east();

                if (pos.getY() <= colorGrabber.level.getMinBuildHeight())
                    break;
                pos = pos.below();
            } while (colorless(pos, colorGrabber));

            this.pixelInfo = colorGrabber.getPixelInfo(pos, ceiling);
        }

        private static boolean colorless(BlockPos pos, ColorGrabber colorGrabber) {
            return colorGrabber.getColor(pos) == MaterialColor.NONE;
        }
    }
}
