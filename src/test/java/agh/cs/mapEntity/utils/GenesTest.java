package agh.cs.mapEntity.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenesTest {

    @Test
    void Genes(){
        Genes genes = new Genes();
        Genes genes2 = new Genes();
        Genes child = new Genes(genes, genes2);

        int []fromGenClass = genes.getGenesArray();
        int []fromGenClass2 = genes2.getGenesArray();
        int []fromGenClassC = child.getGenesArray();

        for (int i=0; i< 32; i++) {
            assertTrue(fromGenClass[i] >= 0 && fromGenClass[i] <= 7);
            assertTrue(fromGenClass2[i] >= 0 && fromGenClass2[i] <= 7);
            assertTrue(fromGenClassC[i] >= 0 && fromGenClassC[i] <= 7);
        }
        int [] typesOfGenes = new int[8];
        int [] typesOfGenes2 = new int[8];
        int [] typesOfGenesFromParents = new int[8];
        for (int i=0; i<32; i++) {
            typesOfGenes[fromGenClass[i]] += 1;
            typesOfGenes2[fromGenClass2[i]] += 1;
            typesOfGenesFromParents[fromGenClassC[i]] += 1;
        }
        for (int i=0; i<8; i++) {
            assertTrue(typesOfGenes[i] > 0);
            assertTrue(typesOfGenes2[i] > 0);
            assertTrue(typesOfGenesFromParents[i] > 0);
        }

    }
}