import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import kim.ian.chembench.DescriptorSet;
import kim.ian.chembench.DescriptorType;
import kim.ian.chembench.ReadDescriptors;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Main {
    /**
     * Writes a descriptor matrix to an X file.
     *
     * @param descriptorSet - a DescriptorSet object representing the matrix
     * @param outfile       - a Path to the output X file
     */
    private static void writeToXFile(DescriptorSet descriptorSet, Path outfile) {
        try (BufferedWriter writer = Files.newBufferedWriter(outfile, StandardCharsets.UTF_8)) {
            // TODO your code goes here
            // (use the already created `writer` object to write out values to the file)
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Path exampleDir = Paths.get("examples").toAbsolutePath();
        String sdfName = "aa_48_2.sdf";
        DescriptorSet cdk = ReadDescriptors.read(exampleDir.resolve(sdfName + ".cdk.x"));
        DescriptorSet dragonH = ReadDescriptors.read(exampleDir.resolve(sdfName + ".dragonH"));
        DescriptorSet dragonNoH = ReadDescriptors.read(exampleDir.resolve(sdfName + ".dragonNoH"));
        DescriptorSet ISIDA = ReadDescriptors.read(exampleDir.resolve(sdfName + ".ISIDA"));
        DescriptorSet maccs = ReadDescriptors.read(exampleDir.resolve(sdfName + ".maccs"));
        DescriptorSet moe2d = ReadDescriptors.read(exampleDir.resolve(sdfName + ".moe2D"));

        // compoundName -> [descriptor1, descriptor2, ...]
        Map<String, List<Double>> cdkMatrix = cdk.getMatrix();
        Map<String, List<Double>> moe2dMatrix = moe2d.getMatrix();

        // join the descriptor names lists together, appending the descriptor type to each descriptor name
        List<String> hybridDescriptorNames = Lists.newArrayList();
        // TODO your code goes here
        // take cdkMatrix and moe2dMatrix and combine them
        Map<String, List<Double>> hybridMatrix = Maps.newTreeMap();
        // TODO your code goes here

        // write out the result to an X file
        DescriptorSet hybridDescriptors = new DescriptorSet();
        hybridDescriptors.setDescriptorType(DescriptorType.HYBRID);
        hybridDescriptors.setDescriptorNames(hybridDescriptorNames);
        hybridDescriptors.setMatrix(hybridMatrix);

        List<String> filenameParts = Lists.newArrayList(sdfName);
        for (DescriptorSet ds : new DescriptorSet[]{cdk, moe2d, hybridDescriptors}) {
            filenameParts.add(ds.getDescriptorType().toString().toLowerCase());
        }
        Joiner joiner = Joiner.on('.');
        String hybridFilename = joiner.join(filenameParts);
        writeToXFile(hybridDescriptors, exampleDir.resolve(hybridFilename));
    }
}
