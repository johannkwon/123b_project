import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class SeqFetch {

    private HashMap<String, String> sequences;
    private HashMap<String, String> alignedSequences;

    public SeqFetch() {
        sequences = new HashMap<String, String>();
        alignedSequences = new HashMap<String, String>();
    }

    public void strain(String genus, File f) throws Exception {
        BufferedReader b = new BufferedReader(new FileReader(f));
        String buffer = "";
        String temp = "";
        String nextLine = "";
        boolean organismSeq = false;

        File nf = new File("data\\" + genus + ".fa");

        FileWriter fw = new FileWriter(nf);
        BufferedWriter br = new BufferedWriter(fw);

        int count = 0;
        while((buffer = b.readLine()) != null) {
            count++;
            if(buffer.indexOf(">") == -1) {
                if(organismSeq) {
                    temp += buffer;
                    //System.out.println(temp + "\n");
                } else {
                    continue;
                }
            } else {
                if(buffer.indexOf("organism=" + genus) != -1) {
                    //System.out.println(buffer);
                    try {
                        br.write(temp);
                        br.newLine();
                        temp = "";
                    } catch(IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        br.newLine();
                        br.write(buffer);
                        br.newLine();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                    organismSeq = true;
                } else {
                    organismSeq = false;
                }
            }

        }

        br.close();
        System.out.println(count);
    }

    public void removeDuplicates(String genus, File f, int limit) throws Exception {
        BufferedReader b = new BufferedReader(new FileReader(f));

        String buffer = "";
        String temp = "";
        String nextLine = "";
        boolean organismSeq = false;

        File nf = new File("data\\" + genus + "_unique_" + limit + ".fa");

        FileWriter fw = new FileWriter(nf);
        BufferedWriter br = new BufferedWriter(fw);

        boolean cont = false;

        System.out.println(b.readLine());

        String sequence = "";
        String def = "";

        b.readLine();
        int count = 0;

        while(!cont) {
            /*
            if(count == limit) {
                break;
            }*/
            buffer = b.readLine();
            def = buffer;
            //System.out.println(def);
            if(def == null) {
                break;
            }
            buffer = b.readLine();
            sequence = buffer;
            //System.out.println(sequence);
            if(sequence == null) {
                break;
            }
            b.readLine();
            sequences.put(sequence, def);
            count++;
        }

        int iterator = 0;
        for(Map.Entry<String, String> seq : sequences.entrySet()) {

            if(iterator == limit) {
                break;
            }
            System.out.println(seq.getValue());
            System.out.println(seq.getKey());
            System.out.println();
            br.write(seq.getValue());
            br.newLine();
            br.write(seq.getKey());
            br.newLine();
            br.newLine();
            iterator++;
        }

        br.close();
        System.out.println("number of sequence addition attempts: " + count);
        System.out.println("number of sequences added to file: " + iterator);
    }

    public void concatenate(File first, File second, String firstGenus, String secondGenus, int firstSize, int secondSize) throws Exception {
        BufferedReader firstRead = new BufferedReader(new FileReader(first));
        BufferedReader secondRead = new BufferedReader(new FileReader(second));

        int sum = firstSize + secondSize;

        File nf = new File("data\\stitched_" + sum + "_" + firstGenus + "_" + secondGenus + ".fa");

        FileWriter fw = new FileWriter(nf);
        BufferedWriter br = new BufferedWriter(fw);

        String buffer = "";
        while((buffer = firstRead.readLine()) != null) {
            br.write(buffer);
            br.newLine();
        }

        while((buffer = secondRead.readLine()) != null) {
            br.write(buffer);
            br.newLine();
        }

        br.close();
    }

    public void splitSeeds(File seeds, String name, int size) throws Exception {
        BufferedReader fileRead = new BufferedReader(new FileReader(seeds));

        File nfEighty = new File("data\\" + name + "_" + size + "_80p.fa");
        FileWriter fwEighty = new FileWriter(nfEighty);
        BufferedWriter brEighty = new BufferedWriter(fwEighty);

        File nfTwenty = new File("data\\" + name + "_" + size + "_20p.fa");
        FileWriter fwTwenty = new FileWriter(nfTwenty);
        BufferedWriter brTwenty = new BufferedWriter(fwTwenty);



        int count = 0;
        boolean cont = false;
        String buffer = "";
        while(!cont) {
            if(count < size * 0.8) {
                buffer = fileRead.readLine();
                System.out.println("buffer " + buffer);
                if(buffer != null) {
                    brEighty.write(buffer);
                } else {
                    break;
                }
                buffer = fileRead.readLine();
                if(buffer != null) {
                    brEighty.write(buffer);
                } else {
                    break;
                }
                fileRead.readLine();
                brEighty.newLine();
            } else {
                buffer = fileRead.readLine();
                if(buffer != null) {
                    brTwenty.write(buffer);
                } else {
                    break;
                }
                buffer = fileRead.readLine();
                if(buffer != null) {
                    brTwenty.write(buffer);
                } else {
                    break;
                }
                fileRead.readLine();
                brTwenty.newLine();
            }
            count++;
        }
        brEighty.close();
        brTwenty.close();
    }


    public static void main(String[] args) throws Exception {
        File readFile = new File("D:\\workspaces\\proj_123b\\data\\fungene_9.9.11_nifH_9724_unaligned_protein_seqs.fa");
        SeqFetch sf = new SeqFetch();

/*
        String nameR = "Rhizobium"; //Mesorhizobium
        File orgFileR = new File("D:\\workspaces\\proj_123b\\data\\" + nameR + ".fa");
        sf.strain(nameR, readFile);
        sf.removeDuplicates(nameR, orgFileR, 50);*/


        String nameM = "Mesorhizobium";
        File orgFileM = new File("D:\\workspaces\\proj_123b\\data\\" + nameM + ".fa");
        sf.strain(nameM, readFile);
        sf.removeDuplicates(nameM, orgFileM, 50);

        System.out.println("total number of unique sequences: " + sf.sequences.keySet().size());
        for(Map.Entry<String, String> seq : sf.sequences.entrySet()) {
            System.out.println(seq.getValue());
        }


        File rUnique = new File("D:\\workspaces\\proj_123b\\data\\Rhizobium_unique_25.fa");
        File mUnique = new File("D:\\workspaces\\proj_123b\\data\\Mesorhizobium_unique_25.fa");
        // sf.concatenate(rUnique, mUnique, "Rhizobium", "Mesorhizobium", 25, 25);

        //sf.splitSeeds(rUnique, "Rhizobium", 25);
        sf.splitSeeds(mUnique, "Mesorhizobium", 25);
    }

}
