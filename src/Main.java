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
import java.util.ArrayList;
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
            Joiner joiner = Joiner.on('\t');
            int row = descriptorSet.getMatrix().size();
            int col = descriptorSet.getDescriptorNames().size();

            writer.write(joiner.join(row, col));
            writer.newLine();
            String name = joiner.join(descriptorSet.getDescriptorNames());
            writer.write(name);
            writer.newLine();
            for (String compoundName : descriptorSet.getMatrix().keySet()) {
                String values = joiner.join(descriptorSet.getMatrix().get(compoundName));
                writer.write(joiner.join(compoundName,values));
                writer.newLine();
            }

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

        // compoundName -> [descriptor1, descriptor2, ...

        // join the descriptor names lists together, appending the descriptor type to each descriptor name
        List<String> hybridDescriptorNames = combineName(cdk, "CDK", moe2d, "MOE2D");

        // TODO your code goes here

        // take cdkMatrix and moe2dMatrix and combine them

        Map<String, List<Double>> hybridMatrix = combine_matrix(cdk, moe2d);

//        // write out the result to an X file
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
    private static String rename(String type, String name){
        return (type + "_" + name);
    }
    private static List<String> combineName(DescriptorSet one, String attach_one, DescriptorSet two, String attach_two) {
        List<String> hybridDesNames = Lists.newArrayList();
        for (int i =0;i<one.getDescriptorNames().size();i++){
            hybridDesNames.add(rename(attach_one, one.getDescriptorNames().get(i)));
        }
        for (int j =0;j<two.getDescriptorNames().size();j++){
            hybridDesNames.add(rename(attach_two, two.getDescriptorNames().get(j)));
        }
        return hybridDesNames;
    }
    private static Map<String, List<Double>>  combine_matrix (DescriptorSet ones, DescriptorSet twos){
        Map<String, List<Double>> one= ones.getMatrix();
        Map<String, List<Double>> two = twos.getMatrix();
        Map<String, List<Double>> hybrid = Maps.newTreeMap();
        // TODO your code goes here
        for (String compoundName : one.keySet()) {
            List<Double> values = one.get(compoundName);
            hybrid.put(compoundName, values);
        }
        for (String compoundName :two.keySet()) {
            if (hybrid.containsKey(compoundName)){
                List<Double> newValues= new ArrayList<Double>();
                List<Double> values = two.get(compoundName);
                newValues.addAll(hybrid.get(compoundName));
                newValues.addAll(values);

                hybrid.put(compoundName, newValues);
            }
            else{
                throw new RuntimeException("Non-match keys");
            }
        }
        return hybrid;
    }
}
