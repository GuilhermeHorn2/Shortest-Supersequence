package misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class main_misc {
	
	
	public static final int MAX = 1_000_000;
	
	
	
	public static void main(String[] args) {
	
	
		List<Integer> big = new ArrayList<>(Arrays.asList(1,5,5,7,9,1));
		List<Integer> small = new ArrayList<>(Arrays.asList(1,5,9));
		
		System.out.println(shortest_seq(big,small));
		
		
	}
	
	private static void swap(List<List<Integer>> bank,int a,int b){
		
		List<Integer> tmp = new ArrayList<>();
		tmp.addAll(bank.get(a));
		bank.set(a,bank.get(b));
		bank.set(b, tmp);
		
	}
	
	private static int partition(List<List<Integer>> bank,int strt,int end){
		
		int pivot = bank.get((strt+end)/2).get(0);
		
		while(strt <= end){
			
			while(bank.get(strt).get(0) < pivot){
				strt++;
			}
			while(bank.get(end).get(0) > pivot){
				end--;
			}
			if(strt <= end) {
				swap(bank,strt,end);
				strt++;
				end--;
			}
		}
		return strt;
	} 
	
	private static void quick_sort(List<List<Integer>> bank,int strt,int end){
		
		int part = partition(bank,strt,end);
		
		if(part-1 > strt ){
			quick_sort(bank,strt,part-1);
		}
		if(end > part){
			quick_sort(bank,part,end);
		}
	}
	
	private static int sub_and_sum(List<Integer> strt,List<Integer> end){
		
		int sum = 0;
		for(int i = 0;i < end.size();i++){
			if(end.get(i)- strt.get(i) == 0){
				return -1;
			}
			sum += end.get(i) - strt.get(i);
		}
		return sum;
	}
	
	private static boolean has_zero(List<Integer> count) {
		
		for(int i = 0;i < count.size();i++) {
			if(count.get(i) == 0){
				return true;
			}
		}
		return false;
	}
	
	private static List<Integer> best_interval(List<List<Integer>> bank,int qnt){
		
		//given a pair of (value,index) find the best interval,qnt is the number of different idx
		
		quick_sort(bank,0,bank.size()-1);
		
		//System.out.println(bank);
		
		//count the quantity of each idx
		
		List<Integer> counter = new ArrayList<>();
		
		for(int i = 0;i < qnt;i++){
			counter.add(0);
		}
		
		List<Integer> cpy = new ArrayList<>();
		cpy.addAll(counter);
		List<List<Integer>> count_list = new ArrayList<>(Arrays.asList(cpy));
		
		int mins_offset = -1;
		
		for(int i = 0;i < bank.size();i++){
			int idx = bank.get(i).get(1);
			counter.set(idx, counter.get(idx)+1);
			cpy = new ArrayList<>();
			cpy.addAll(counter);
			count_list.add(cpy);
			
			if(!has_zero(cpy)) {
				mins_offset = i-1;
			}
			
		}
		
		
		int min = -1;
		int x = 0;
		int y = 0;
		for(int i = count_list.size()-1;i > mins_offset;i--){
			
			List<Integer> max_count = count_list.get(i);
			
			for(int j = 0;j <= mins_offset;j++){
				
				List<Integer> min_count = count_list.get(j);
				
				int sum = sub_and_sum(min_count,max_count);
				
				if(sum != -1){
					
					if(min == -1) {
						min = sum;
						x = i;
						y = j+1;
					}
					else {
						if(sum < min) {
							min = sum;
							x = i;
							y = j+1;
						}
					}
					
				}
				
			}
			
		}
		
		
		int max_bound = bank.get(x-1).get(0);
		int min_bound = bank.get(y-1).get(0);
		
		return new ArrayList<>(Arrays.asList(min_bound,max_bound));
	}
	
	private static List<Integer> shortest_seq(List<Integer> big,List<Integer> small){
		
		//type --> index
		HashMap<Integer,List<Integer>> map = new HashMap<>();
		
		int qnt = 0;
		for(int i = 0;i < small.size();i++){
			
			int x = small.get(i);
			for(int j = 0;j < big.size();j++){
				
				if(big.get(j) == x){
					if(!map.containsKey(qnt)){
						map.put(qnt,new ArrayList<>(Arrays.asList(j)));
					}
					else {
						map.get(qnt).add(j);
					}
				}
			}
			qnt++;
		}
		
		List<List<Integer>> bank = new ArrayList<>();
		for(int i = 0;i < qnt;i++){
			
			List<Integer> idxs = map.get(i);
			for(int j = 0;j < idxs.size();j++){
				bank.add(new ArrayList<>(Arrays.asList(idxs.get(j),i)));
			}
			
		}
		
		//System.out.println(bank);
		
		
		
		
		return best_interval(bank,qnt);
	}
	
}
