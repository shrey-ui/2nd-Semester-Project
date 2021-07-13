import java.text.NumberFormat;



import java.util.ArrayList;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import javax.lang.model.util.Elements;



import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class scraper {
	
	static Scanner scan= new Scanner(System.in);
	
	static String rupeeFormat(String value){
			
		value=value.replace(",","");
        char lastDigit=value.charAt(value.length()-1);
        String result = "";
        int len = value.length()-1;
        int nDigits = 0;

        for (int i = len - 1; i >= 0; i--)
         {
             result = value.charAt(i) + result;
             nDigits++;
             if (((nDigits % 2) == 0) && (i > 0))
             {
                 result = "," + result;
             }
         }
         return (result+lastDigit);
     }
	
	static String futures_buy() {
		final String url= "https://zerodha.com/margin-calculator/Futures/";
		try {
			int initi_amt= 1000000;
			
			scan.nextLine();
			
			Map<String, Integer> month= new HashMap<>();
			
			
			ArrayList<String> tckrinfo= new ArrayList<>();
			ArrayList<Integer> exp_month= new ArrayList<>();
			ArrayList<Integer> lots= new ArrayList<>();
			
			
			
			String choice="yes";
			int c=0;
			
			while(choice.toLowerCase().equals("yes")) {
				
				
				System.out.print("Enter the TICKER name: ");
				tckrinfo.add(scan.nextLine());
			
				System.out.print("Expiry month? (1/2/3): ");
				exp_month.add(scan.nextInt());
				
				c++;
				
				System.out.print("How many Lots? ");
				lots.add(scan.nextInt());
				
				
				scan.nextLine();
				System.out.print("Do you wish to continue and add more Future Contracts?(yes/no): ");
				choice= scan.nextLine();
				
				System.out.println("");
				
				
			}
			
			
			
			
			
		
			
			
			
			final Document doc1= Jsoup.connect(url).get();
			
			
			
			ArrayList<ArrayList<String>> final_info= new ArrayList<>();
			ArrayList<Float> buy_price= new ArrayList<>();
			ArrayList<Integer> lotsize= new ArrayList<>();
			
			int ct=0;
			for(String k: tckrinfo) {
				
			
			for(Element row: doc1.select("table.data.futures tr")) {
				
				
				
				if(row.select("strong").text().equals(k) ) {
					final_info.add(new ArrayList<String>());
					
					String mar= row.select("td.nrml.text-right").first().text();
					
					
					
					String exp= row.select("span.scrip-expiry.text-12.text-grey").text();
					
					float price= Float.parseFloat(row.select("td.price.text-right").text());
					buy_price.add(price);
					
					String lot= row.select("div.table-sub-info.text-12").first().select("span").text();
					
					lotsize.add(Integer.parseInt(lot));
					
					
					
					
					
					final_info.get(ct).add(0, k);
					final_info.get(ct).add(1, mar);
					final_info.get(ct).add(2, exp);
					
					
					ct++;
					
				}else
					continue;
			}
			
			
			}
			
		
			
			
			
			
			int tot_margin_req=0;
			
			
			for(int i=0 ;i<tckrinfo.size();i+=1) {
				System.out.println(final_info.get(3*i + exp_month.get(i)-1));
				
				System.out.println("The Lot size is: "+ lotsize.get(3*i+ exp_month.get(i)-1));
				
				
				System.out.println("Your buying price per share is: "+buy_price.get(3*i + exp_month.get(i)-1));
				
				tot_margin_req+= Integer.parseInt(final_info.get(3*i+ exp_month.get(i)-1).get(1));
				
				System.out.println("");
				
				
				
				
			}
			
			return ("The Total Margin Requirement for your Trade is Rs. "+rupeeFormat(String.valueOf(tot_margin_req)));
			
			
			
			
			
			
		}catch(Exception es) {
			es.printStackTrace();
			
			
		}
		
		return "Your Transacation could not be "
		+ "completed because of an error. Please try again.";
		
	}
	
	
	static void stockinfo(String finurl)
	{
		try {
		Document docfin1= Jsoup.connect(finurl).get();
		
		 
		
		
		org.jsoup.select.Elements row1= docfin1.select("table tr:eq(0) > td:eq(1)");
		
		System.out.println("CURRENT TRADING PRICE "+row1.first().text());
		String cmpdes= docfin1.select("div.morepls_cnt").text();
		
		String[]cmpdes_words= cmpdes.split(" ");
	
		System.out.println("A short Description: \n");
		
		
		for(int i=0; i<cmpdes_words.length; i++) {
			System.out.print(cmpdes_words[i]+ " ");
			
			
			if(i%10==0 && i>0)
				
				System.out.println("");
			else continue;
		}
		}catch(Exception es) {
			es.printStackTrace();
		}
		
		
		
	}
	static void cash_mark() {
		
		final String url="https://www.moneycontrol.com/india/stockpricequote/";
		
		try {
		Document doccash= Jsoup.connect(url).get();
		
		
		scan.nextLine();
		
		System.out.print("Enter Company name: ");
		
		String stckname= scan.nextLine();
		stckname= stckname.toLowerCase();
		
		for(Element row: doccash.select("table.pcq_tbl.mt10 tr")) {
			for(Element name: row.select("tr").select("td > a")) {
				if(name.attr("title").toLowerCase().equals(stckname)) {
					int choice;
					System.out.println("Do You want: \n1) Detailed rundown or \n2) Brief Intro ");
					choice= scan.nextInt();
					if(choice==1)
						java.awt.Desktop.getDesktop().browse(new java.net.URI(name.attr("href")));
					else
						stockinfo(name.attr("href"));
				}
				
				
				
			}
			
			}
		
			
		
		
		
		
		
		}catch(Exception es) {
			es.printStackTrace();
			System.out.println( "YOUR REQUEST COULD NOT BE COMPLETED "
					+ "DUE TO SOME ISSUE. PLEASE TRY AGAIN");
		}
		
	}
	
	final static int amtini= 1000000;
	
	static int cashmo=0;
	static int margin_money= 0;
	
	ArrayList<ArrayList<String>> cash_hold= new ArrayList<>();
	
	ArrayList<ArrayList<String>> fut_hold= new ArrayList<>();
	
	
	public static void main(String[] args) {
		
		System.out.println("DO YOU WISH TO BUY 1) FUTURESCONTRACTS OR 2) PHYSICAL STOCKS?");
		
		int choice= scan.nextInt();
		
		if(choice==1) {
			System.out.println(futures_buy());
			
			
			System.out.println("DO You WISH TO EXECUTE THE ORDER? (order will be filled in 5 minutes) (Yes/No): ");
			String choicefin= scan.nextLine()
;		}else {
	
	
			cash_mark();
			
			
		}
		

	
	
	}

}
















