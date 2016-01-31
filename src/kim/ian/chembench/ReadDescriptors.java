package kim.ian.chembench;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ReadDescriptors {

    private static final int NUM_MACCS_KEYS = 400;

    private static void readDragonDescriptors(Path infile, List<String> descriptorNames, List<Descriptors> descriptorValueMatrix) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(infile, StandardCharsets.UTF_8)) {
            // values for each molecule
            List<String> descriptorValues;
            // junk line, should say "dragonX: Descriptors"
            String line = br.readLine();

            // contains some numbers
            line = br.readLine();
            Scanner tok = new Scanner(line);
            // int num_molecules = Integer.parseInt(tok.next());

            // just says "2" all the time, no idea what that means, so skip that
            tok.next();
            // int num_descriptors = Integer.parseInt(tok.next());

            // the descriptor names are on this line
            line = br.readLine();
            tok.close();
            tok = new Scanner(line);
            while (tok.hasNext()) {
                String dname = tok.next();
                descriptorNames.add(dname);
            }
            tok.close();

            // contains molecule name, which isn't a descriptor
            descriptorNames.remove(1);
            descriptorNames.remove(0);

            // read in the descriptor values. If one of them is the word "Error", Dragon failed at descriptoring.
            Joiner joiner = Joiner.on(' ');
            while ((line = br.readLine()) != null) {
                tok = new Scanner(line);
                descriptorValues = Lists.newArrayList();
                while (tok.hasNext()) {
                    String dvalue = tok.next();
                    if (dvalue.equalsIgnoreCase("Error")) {
                        tok.close();
                        throw new IOException("Dragon descriptors invalid!");
                    }
                    descriptorValues.add(dvalue);
                }
                tok.close();

                Descriptors di = new Descriptors();
                // contains molecule name, which isn't a descriptor
                di.setCompoundName(descriptorValues.remove(1));
                di.setCompoundIndex(Integer.parseInt(descriptorValues.remove(0)));
                di.setDescriptorValues(joiner.join(descriptorValues));
                descriptorValueMatrix.add(di);
                descriptorValues.clear();
            }
        }
    }

    private static void readMaccsDescriptors(Path infile, List<String> descriptorNames, List<Descriptors> descriptorValueMatrix) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(infile, StandardCharsets.UTF_8)) {
            // first line is junk, it says "name,FP:MACCS."
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String descriptorString = "";
                Scanner tok = new Scanner(line);
                tok.useDelimiter(",");
                String compoundName = tok.next(); // skip compound identifier
                String tmp = tok.next();
                tok.close();
                tok = new Scanner(tmp);
                tok.useDelimiter(" ");
                int last = 0;
                int descriptor = 0;
                while (tok.hasNext()) {
                    descriptor = Integer.parseInt(tok.next());
                    for (int i = last; i < descriptor; i++) {
                        descriptorString += "0 ";
                    }
                    descriptorString += "1 ";
                    last = descriptor + 1;
                }
                tok.close();
                for (int i = last; i < NUM_MACCS_KEYS; i++) {
                    descriptorString += "0 ";
                }
                Descriptors di = new Descriptors();
                di.setDescriptorValues(descriptorString);
                di.setCompoundName(compoundName);
                descriptorValueMatrix.add(di);

            }

            Joiner joiner = Joiner.on(' ');
            ContiguousSet<Integer> keys = ContiguousSet.create(Range.closedOpen(0, NUM_MACCS_KEYS), DiscreteDomain.integers());
            for (Integer key : keys) {
                descriptorNames.add(key.toString());
            }
        }
    }

    private static void readMoe2dDescriptors(Path infile, List<String> descriptorNames, List<Descriptors> descriptorValueMatrix) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(infile, StandardCharsets.UTF_8)) {
            /* contains descriptor names */
            String line = br.readLine();
            Scanner tok = new Scanner(line);
            tok.useDelimiter(",");
            /* first descriptor says "name"; we don't need that. */
            tok.next();
            while (tok.hasNext()) {
                descriptorNames.add(tok.next());
            }
            String compoundName = null;
            while ((line = br.readLine()) != null) {
                tok = new Scanner(line);
                tok.useDelimiter(",");
                if (tok.hasNext()) {
                /* first descriptor value is the name of the compound */
                    compoundName = tok.next();
                }
                String descriptorString = "";
                while (tok.hasNext()) {
                    String val = tok.next();
                    if (val.contains("NaN") || val.contains("e")) {
                    /*
                     * there's a divide-by-zero error for MOE2D sometimes.
                     * Results in NaN or "e+23" type numbers. only happens on
                     * a few descriptors, so it should be OK to just call it a
                     * 0 and move on.
                     */
                        val = "0";
                    }
                    descriptorString += val + " ";
                }
                if (!descriptorString.equalsIgnoreCase("")) {
                    Descriptors di = new Descriptors();
                    di.setDescriptorValues(descriptorString);
                    di.setCompoundName(compoundName);
                    descriptorValueMatrix.add(di);
                }
                tok.close();
            }
        }
    }

    private static void readIsidaDescriptors(Path infile, List<String> descriptorNames, List<Descriptors> descriptorValueMatrix) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(infile, StandardCharsets.UTF_8)) {
        /* contains descriptor names */
            String line = br.readLine();
            Scanner tok = new Scanner(line);
            tok.useDelimiter(" ");
        /* first descriptor says "title"; we don't need that. */
            tok.next();
            while (tok.hasNext()) {
                descriptorNames.add(tok.next());
            }
            String compoundName = null;
            while ((line = br.readLine()) != null) {
                tok = new Scanner(line);
                tok.useDelimiter(" ");
                if (tok.hasNext()) {
                /* first descriptor value is the name of the compound */
                    compoundName = tok.next();
                }
                String descriptorString = "";
                while (tok.hasNext()) {
                    String val = tok.next();
                    descriptorString += val + " ";
                }
                if (!descriptorString.equalsIgnoreCase("")) {
                    Descriptors di = new Descriptors();
                    di.setCompoundName(compoundName);
                    di.setDescriptorValues(descriptorString);
                    descriptorValueMatrix.add(di);
                }
                tok.close();
            }
        }
    }

    private static void readXDescriptors(Path infile, List<String> descriptorNames, List<Descriptors> descriptorValueMatrix) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(infile, StandardCharsets.UTF_8)) {
            String line = br.readLine(); // header. ignored.
            line = br.readLine(); // contains descriptor names
            Scanner tok = new Scanner(line);
            tok.useDelimiter("\\s+");
            while (tok.hasNext()) {
                descriptorNames.add(tok.next());
            }
            tok.close();

            while ((line = br.readLine()) != null) {
                tok = new Scanner(line);
                tok.useDelimiter("\\s+");
                Descriptors di = new Descriptors();
                if (tok.hasNext()) {
                    di.setCompoundIndex(Integer.parseInt(tok.next())); // first value is the index of the compound
                }
                if (tok.hasNext()) {
                    di.setCompoundName(tok.next()); // second value is the name of the compound
                }
                String descriptorString = "";
                while (tok.hasNext()) {
                    descriptorString += tok.next() + " ";
                }
                if (!descriptorString.equalsIgnoreCase("")) {
                    di.setDescriptorValues(descriptorString);
                    descriptorValueMatrix.add(di);
                }
                tok.close();
            }
        }
    }

    /**
     * Reads in a raw descriptor output file and returns a DescriptorSet object for that file.
     *
     * @param infile - a Path to the input file
     * @return a DescriptorSet object representing the descriptor set provided.
     */
    public static DescriptorSet read(Path infile) {
        if (!Files.exists(infile)) {
            throw new IllegalArgumentException("No such file: " + infile);
        }

        DescriptorType infileType = null;
        DescriptorType[] descriptorTypes = DescriptorType.values();
        for (int i = 0; i < descriptorTypes.length && infileType == null; i++) {
            DescriptorType currType = descriptorTypes[i];
            // XXX Path#endsWith and String#endsWith don't mean the same thing
            if (infile.toString().endsWith(currType.getExtension())) {
                infileType = currType;
            }
        }
        if (infileType == null) {
            throw new IllegalArgumentException("Input file has an unrecognized descriptor set extension");
        }

        List<String> descriptorNames = Lists.newArrayList();
        List<Descriptors> descriptorValueMatrix = Lists.newArrayList();
        try {
            switch (infileType) {
                case CDK:
                    readXDescriptors(infile, descriptorNames, descriptorValueMatrix);
                    break;
                case DragonH:
                case DragonNoH:
                    readDragonDescriptors(infile, descriptorNames, descriptorValueMatrix);
                    break;
                case MACCS:
                    readMaccsDescriptors(infile, descriptorNames, descriptorValueMatrix);
                    break;
                case MOE2D:
                    readMoe2dDescriptors(infile, descriptorNames, descriptorValueMatrix);
                    break;
                case ISIDA:
                    readIsidaDescriptors(infile, descriptorNames, descriptorValueMatrix);
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException("Descriptor reading failed", e);
        }

        Map<String, List<Double>> matrix = Maps.newTreeMap();
        Splitter splitter = Splitter.on(CharMatcher.WHITESPACE).omitEmptyStrings();
        for (Descriptors d : descriptorValueMatrix) {
            String compoundName = d.getCompoundName();
            List<String> rawValues = splitter.splitToList(d.getDescriptorValues());
            List<Double> values = Lists.newArrayListWithExpectedSize(rawValues.size());
            for (String rawValue : rawValues) {
                try {
                    values.add(Double.parseDouble(rawValue));
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Encountered non-double descriptor value", e);
                }
            }
            assert values.size() == descriptorNames.size();
            matrix.put(compoundName, values);
        }

        DescriptorSet ds = new DescriptorSet();
        ds.setDescriptorType(infileType);
        ds.setMatrix(matrix);
        ds.setDescriptorNames(descriptorNames);
        return ds;
    }
}
