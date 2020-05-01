package agh.cs.mapEntity.utils;

import java.util.Random;
import java.util.Arrays;

public class Genes {

    public static final int NROFGENES=32;
    private static final int NROFDIRECTIONS = 8;
    private int[] genes;
    private Random rand = new Random();


    public Genes()
    {
        int[] genesRand = new int[NROFDIRECTIONS];
        for (int g : genesRand) {
            g = 0;
        }
        for(int i=0;i<NROFGENES;i++)
        {
            genesRand[rand.nextInt(NROFDIRECTIONS)]++;
        }

        this.genes=toGenesArray(genEvrGeneInSet(genesRand));
    }

    /* input: array[NROFDIRECTIONS] */
    private int[] genEvrGeneInSet(int[] geneSet)
    {
        int randGene;
        for(int i=0;i<NROFDIRECTIONS;i++)
        {
            if(geneSet[i]==0)
            {
                do {
                    randGene = rand.nextInt(NROFDIRECTIONS);
                }while(geneSet[randGene]<=1);
                geneSet[i]++;
                geneSet[randGene]--;

            }
        }
        return geneSet;
    }

    public Genes(Genes parent1, Genes parent2 )
    {
        /* Generating 2 position that will divide 30 Genes into 3 parts */
        int randInt1 = rand.nextInt(NROFGENES-1);
        int randInt2;
        do {
            randInt2 = rand.nextInt(NROFGENES-1);
        }while (randInt1==randInt2);

        /* switching so second is bigger */
        if(randInt1>randInt2)
        {
            int tmp=randInt1;
            randInt1=randInt2;
            randInt2=tmp;
        }

        int []newGenes;
        int []onePartGen;
        int []twoPartGen;
        /* which parent gives one part */
        if(rand.nextBoolean())
        {
            onePartGen=parent1.getGenesArray();
            twoPartGen=parent2.getGenesArray();
        }
        else{
            twoPartGen=parent1.getGenesArray();
            onePartGen=parent2.getGenesArray();
        }

        newGenes= Arrays.copyOf(twoPartGen, NROFGENES);
        System.arraycopy(onePartGen, randInt1+1, newGenes, randInt1+1, randInt2-randInt1);

        /* generating array with NROFDIRECTIONS counted */
        int[] genes = new int[NROFDIRECTIONS];
        for (int g : genes) {
            g = 0;
        }

        for (int g : newGenes) {
            genes[g]++;
        }

        this.genes=toGenesArray(genEvrGeneInSet(genes));
    }

    /* input 8 genes, output 32 dir genes */
    private int[] toGenesArray(int[] genesRand)
    {
        int[] gArr = new int[NROFGENES];
        int tmpNrGenes;
        int j=0;
        for (int i=0;i<8;i++)
        {
            tmpNrGenes=genesRand[i];
            while(tmpNrGenes>0){
                gArr[j]=i;
                tmpNrGenes--;
                j++;
            }
        }
        return gArr;
    }

    public int[] getGenesArray()
    {
        return this.genes;
    }

}
