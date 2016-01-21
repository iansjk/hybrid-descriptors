# hybrid-descriptors
**Assignment**: combine two descriptor set files of any type into a new, valid `.x` file containing the descriptors of both sets.

### Requirements
- Output file is a valid `.x` file
    - The first line representing the shape of the matrix must be correct
    - Fields should be delimited by the tab character (`\t`)
- Descriptor names in the new `.x` file are prefixed by `DescriptorSet.descriptorType` and an underscore (`_`)
    - For example, a descriptor named `ALogP` in the CDK descriptor set should be renamed to `CDK_ALogP` in the hybrid `.x` file

### Provided resources
- The `ReadDescriptors#read` method will take a Java `Path` and return a `DescriptorSet` object for any descriptor set type
    - `DescriptorSet.descriptorType` is the descriptor set type (see the `DescriptorType` enum for list of values)
    - `DescriptorSet.matrix` is a `Map<String, Map<String, Double>>` object representing the descriptor matrix
        - The outer map is a mapping of compound names to the inner map
        - The inner map is a mapping of descriptor names to descriptor values

- The `examples/` directory contains the descriptor set files for the `Anticonvulsants 48` dataset

### Tips
- The Guava library (included in `lib/`) will be helpful for writing the `.x` file
    - Consider using the `Joiner` class when writing fields:
    ```
    Joiner joiner = Joiner.on('\t');
    joiner.join(Lists.newArrayList(3, 5, 7)); # produces "3\t5\t7"
    ```
