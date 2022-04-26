Final Project for CS123B

SeqFetch.java is used for parsing data from FASTA files for easier and more specific use.

It contains the following methods:
- strain(): Filters sequences by looking for the genus in the defline
- removeDuplicates(): Filters duplicate sequences from a file using a HashMap with the sequence as the key and the defline as the value.
- concatenate(): Unused, can be used to "stitch" files together
- splitSeeds(): Unused, can be used to split an unaligned FASTA file into two files, one containing 80% of the sequences and the other containing 20% of the sequences
- identify(): Finds deflines for a list of accession numbers