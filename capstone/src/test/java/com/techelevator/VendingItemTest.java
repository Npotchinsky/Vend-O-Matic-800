package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class VendingItemTest {
    private ByteArrayOutputStream output;

    @Test
       public void crunchSoundTest(){
        VendingItem crunchTest = new VendingItem("A1","crunchy test",3.95,"Chip");
        Assert.assertEquals("Crunch Crunch, Yum!", crunchTest.getEatingSounds());
    }

    @Test
        public void chewSoundTest(){
        VendingItem chewTest = new VendingItem("A1", "chewy test", 3.5, "Gum");
        Assert.assertEquals("Chew Chew, Yum!", chewTest.getEatingSounds());
    }


    @Test

        public void buyTest(){
        PrintStream original = System.out;
        output = new ByteArrayOutputStream();
        PrintStream tps = new PrintStream(output);
        VendingItem buyTest  = new VendingItem("A1", "buy test", 2.90, "Drink");
        buyTest.buyItem();
        Assert.assertEquals(4, buyTest.getQuantity());
        buyTest.buyItem();
        buyTest.buyItem();
        Assert.assertEquals(2,buyTest.getQuantity());
        buyTest.buyItem();
        buyTest.buyItem();
        Assert.assertEquals(0, buyTest.getQuantity());
        System.setOut(tps);
        buyTest.buyItem();
        System.setOut(original);
        tps.flush();
        String expected = "Sold out." + System.lineSeparator();
        Assert.assertEquals(expected, output.toString());

    }
}
