package com.techelevator;

import com.techelevator.view.Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss a");

	public void run() {
		VendingLog.clearLog();
		File file = new File("capstone/vendingmachine.csv");
		List<VendingItem> vendingItemList = new ArrayList<>();
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String[] s = scanner.nextLine().split("\\|");
				vendingItemList.add(new VendingItem(s[0],s[1],Double.parseDouble(s[2]),s[3]));
			}
		} catch (FileNotFoundException e) {

			System.out.println("Whoops");
		}
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				for (VendingItem item:vendingItemList) {
					System.out.print(item.getSlot()+" "+item.getName()+" "+ currency.format(item.getPrice())+ " ");
					if (item.getQuantity()>0){
						System.out.println(item.getQuantity());
					}
					else {
						System.out.println("Sold out");
					}
				}
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				Menu purchasingMenu = new Menu(System.in,System.out);
				BigDecimal moneyProvided = new BigDecimal(0.00);

				while (true) {
					System.out.println("\n Current Money Provided:  " + currency.format(moneyProvided));
					String purchasingChoice = (String) purchasingMenu.getChoiceFromOptions(PURCHASING_MENU_OPTIONS);
					if (purchasingChoice.equals(PURCHASING_MENU_OPTION_FEED_MONEY)){
						System.out.println("Please input a valid whole dollar amount($1,$2,$5,$10): ");
						double money = Double.parseDouble(scanner.next());
						if(money == 1 || money == 2 || money == 5 || money == 10) {
							moneyProvided = moneyProvided.add(BigDecimal.valueOf(money));
							VendingLog.Log(String.valueOf(LocalDateTime.now().format(formatter) + " FEED MONEY " +
									currency.format(money) + " " + currency.format(moneyProvided)));
						}
						else {
							System.out.println("Invalid Input");
						}
					}
					else if (purchasingChoice.equals(PURCHASING_MENU_OPTION_SELECT_PRODUCT)) {
						for (VendingItem item : vendingItemList) {
							System.out.print(item.getSlot() + " " + item.getName() + " " + currency.format(item.getPrice()) + " ");
							if (item.getQuantity() > 0) {
								System.out.println(item.getQuantity());
							} else {
								System.out.println("Sold out");
							}

						}
						System.out.println("Please make your selection: ");
						String s = scanner.next();
						for (int i = 0; i <= vendingItemList.size(); i++) {
							if (i == vendingItemList.size()) {
								System.out.println("Invalid Selection");
								break;
							} else if (vendingItemList.get(i).getSlot().equals(s)) {
								if (moneyProvided.compareTo(vendingItemList.get(i).getPrice()) == 1
										&& vendingItemList.get(i).getQuantity() > 0) {
									moneyProvided = moneyProvided.subtract(vendingItemList.get(i).getPrice());
									vendingItemList.get(i).buyItem();
									System.out.println(vendingItemList.get(i).getName() + " " + currency.format(vendingItemList.get(i).getPrice()) + " " + currency.format(moneyProvided));
									System.out.println(vendingItemList.get(i).getEatingSounds());
									VendingLog.Log(String.valueOf(LocalDateTime.now().format(formatter) + " " + vendingItemList.get(i).getName() + " " + vendingItemList.get(i).getSlot() + " " +
											currency.format(moneyProvided.add(vendingItemList.get(i).getPrice())) + " " + currency.format(moneyProvided)));
									break;


								} else if (vendingItemList.get(i).getQuantity() <= 0) {
									System.out.println("Item Sold Out");
									break;
								} else {
									System.out.println("Insufficient funds");
									break;
								}

							}

						}
					}


					else if (purchasingChoice.equals(PURCHASING_MENU_OPTION_FINISH_TRANSACTION)){
						int quarters = 0;
						int dimes = 0;
						int nickles = 0;
						BigDecimal moneyRemaining = moneyProvided;


						while (moneyProvided.compareTo(BigDecimal.valueOf(0.0))==1){
							if(moneyProvided.subtract(new BigDecimal("0.25")).compareTo(BigDecimal.valueOf(0.0))!=-1){
								moneyProvided = moneyProvided.subtract(new BigDecimal("0.25"));
								quarters++;
							}
							else if (moneyProvided.subtract(new BigDecimal("0.10")).compareTo(BigDecimal.valueOf(0.0))!=-1){
								moneyProvided = moneyProvided.subtract(new BigDecimal("0.10"));
								dimes++;
							}
							else if(moneyProvided.subtract(new BigDecimal("0.05")).compareTo(BigDecimal.valueOf(0.0))!=-1){
								moneyProvided = moneyProvided.subtract(new BigDecimal("0.05"));
								nickles++;
							}
						}
						System.out.println("Your change is " + quarters + " quarters, "+ dimes + " dimes, and "+ nickles + " nickles.");
						VendingLog.Log(LocalDateTime.now().format(formatter) + " GIVE CHANGE " +
								currency.format(moneyRemaining) + " " + currency.format(moneyProvided));
						break;
					}
				}

			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
				break;
			}
			else if (choice.equals("4")){
				String pathName = "capstone/salesreport" + LocalDateTime.now().format(formatter) + ".txt";
				pathName = pathName.replace(":", "-");
				File salesReport = new File(pathName);
				BigDecimal totalRevenue = new BigDecimal("0");
				try {
					salesReport.createNewFile();
					FileWriter salesReporter = new FileWriter(salesReport, true);
					for (VendingItem item:vendingItemList) {

						int itemQuantitySold = Math.abs(item.getQuantity()-5);
						String out = item.getName() + "|" + itemQuantitySold + "\n";
						totalRevenue = totalRevenue.add(item.getPrice().multiply(BigDecimal.valueOf(itemQuantitySold)));
						salesReporter.append(out);
					}
					String revenueString = "Total Revenue: " + totalRevenue;
					salesReporter.append(revenueString);
					salesReporter.close();



				} catch (IOException e) {
					System.err.println("IOException occurred");
				}
				//Make a file
				//get list of items
				//get number of items sold
				//calculate total sales
			}

		}
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}
}
