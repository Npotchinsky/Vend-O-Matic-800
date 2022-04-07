package com.techelevator;

import com.techelevator.view.Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT };
	private static final String PURCHASING_MENU_OPTION_FEED_MONEY = "Feed Money";
	private static final String PURCHASING_MENU_OPTION_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASING_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASING_MENU_OPTIONS = {PURCHASING_MENU_OPTION_FEED_MONEY, PURCHASING_MENU_OPTION_SELECT_PRODUCT,PURCHASING_MENU_OPTION_FINISH_TRANSACTION};
	private Menu menu;
	Scanner scanner = new Scanner(System.in);
	NumberFormat currency = NumberFormat.getCurrencyInstance();
	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {
		File file = new File("capstone/vendingmachine.csv");
		List<VendingItem> vendingItemList = new ArrayList<>();
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String[] s = scanner.nextLine().split("\\|");
				vendingItemList.add(new VendingItem(s[0],s[1],Double.parseDouble(s[2]),s[3]));
			}
		} catch (FileNotFoundException e) {

			System.out.println("Woops");
		}
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				for (VendingItem item:vendingItemList) {
					System.out.print(item.getSlot()+" "+item.getName()+" "+ item.getPrice()+ " ");
					if (item.getQuantity()>0){
						System.out.println(item.getQuantity());
					}
					else {
						System.out.println("Sold out");
					}
				}
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				Menu purchasingMenu = new Menu(System.in,System.out);
				double moneyProvided = 0.00;
				while (true) {
					System.out.println("\n Current Money Provided:  "+currency.format(moneyProvided));
					String purchasingChoice = (String) purchasingMenu.getChoiceFromOptions(PURCHASING_MENU_OPTIONS);
					if (purchasingChoice.equals(PURCHASING_MENU_OPTION_FEED_MONEY)){
						System.out.println("Please input a valid whole dollar amount($1,$2,$5,$10,): ");
						moneyProvided += Double.parseDouble(scanner.next());
					}
					else if (purchasingChoice.equals(PURCHASING_MENU_OPTION_SELECT_PRODUCT)) {
						for (VendingItem item : vendingItemList) {
							System.out.print(item.getSlot() + " " + item.getName() + " " + item.getPrice() + " ");
							if (item.getQuantity() > 0) {
								System.out.println(item.getQuantity());
							} else {
								System.out.println("Sold out");
							}

						}
						System.out.println("Please make your selection: ");
						String s = scanner.next();
						for (VendingItem item : vendingItemList){
							if(item.getSlot().equals(s)){
								if(item.getPrice() <= moneyProvided){
									moneyProvided -= item.getPrice();
									item.buyItem();
									System.out.println(item.getName()+ " "+ currency.format(item.getPrice())+ " "+ currency.format(moneyProvided));
									System.out.println(item.getEatingSounds());
								}
								else {
									System.out.println("Insufficient funds");
								}
							}
						}

					}
					else if (purchasingChoice.equals(PURCHASING_MENU_OPTION_FINISH_TRANSACTION)){
						int quarters = 0;
						int dimes = 0;
						int nickles = 0;
						BigDecimal moneyRemaining = BigDecimal.valueOf(moneyProvided);

						while (moneyRemaining.compareTo(BigDecimal.valueOf(0.0))==1){
							if(moneyRemaining.subtract(new BigDecimal("0.25")).compareTo(BigDecimal.valueOf(0.0))!=-1){
								moneyRemaining = moneyRemaining.subtract(new BigDecimal("0.25"));
								quarters++;
							}
							else if (moneyRemaining.subtract(new BigDecimal("0.10")).compareTo(BigDecimal.valueOf(0.0))!=-1){
								moneyRemaining = moneyRemaining.subtract(new BigDecimal("0.10"));
								dimes++;
							}
							else if(moneyRemaining.subtract(new BigDecimal("0.05")).compareTo(BigDecimal.valueOf(0.0))!=-1){
								moneyRemaining = moneyRemaining.subtract(new BigDecimal("0.05"));
								nickles++;
							}

						}
						System.out.println("Your change is " + quarters + " quarters, "+ dimes + " dimes, and "+ nickles + " nickles.");
						break;
					}
				}

			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
				break;
			}
		}
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}
}
