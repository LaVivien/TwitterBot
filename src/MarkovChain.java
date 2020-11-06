
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.*;

/*
 * use a MarkovChain to model tweets by gathering the frequency information from a Twitter feed. We
 * can use the MarkovChain to generate "plausible" tweets by conducting a random
 * walk through the chain according to the frequencies. 
 */

public class MarkovChain implements Iterator<String> {
	final NumberGenerator ng;
	final Map<String, ProbabilityDistribution<String>> chain = new LinkedHashMap<String, ProbabilityDistribution<String>>();
	final ProbabilityDistribution<String> startWords;
	
	Iterator<Map.Entry<String, ProbabilityDistribution<String>>> it;
	boolean stop = false;
	String currWord;
	
	public MarkovChain() {
		this(new RandomNumberGenerator());		
	}

	public MarkovChain(NumberGenerator ng) {
		if (ng == null) {
			throw new IllegalArgumentException("NumberGenerator input cannot be null");
		}
		this.ng = ng;
		this.startWords = new ProbabilityDistribution<String>();
		reset();
	}
	
	//adds a bigram to the Markow chain
	void addBigram(String first, String second) {
		if(!chain.containsKey(first)) 
			chain.put(first, new ProbabilityDistribution<String>());
		ProbabilityDistribution<String> pd = chain.get(first);
		pd.record(second);
		chain.put(first,pd);
	}

	//add a sentence training data to the Markov chain frequency
	public void train(Iterator<String> sentence) {		
		String s1 = sentence.next();
		String s2 = "";
		while(sentence.hasNext()) {
			s2 = sentence.next();		
			addBigram(s1, s2);
			s1 = s2;
		}
		addBigram(s1,null);		
	}

	ProbabilityDistribution<String> get(String token) {
		return chain.get(token);
	}

	//set up the iterator from the given starting string
	public void reset(String start) {
		if (start == null) {
			stop = true;
			return;
		}		
		 it = chain.entrySet().iterator();
		 while(it.hasNext()) {
			 Map.Entry<String, ProbabilityDistribution<String>> entry = it.next();
			 String c = entry.getKey();
			 if (c.equals(start)) {
				 currWord = start;
				 break;
			 }
		 }
	}

	public void reset() {
		if (startWords.getTotal() == 0) {
			reset(null);
		} else {
			reset(startWords.pick(ng));
		}
	}

	@Override
	public boolean hasNext() {
		return currWord!=null;
	}

	@Override
	public String next() {
		String  returnString = currWord;		
		ProbabilityDistribution<String> p = chain.get(currWord);
		currWord = p.pick(ng);
		return returnString;
	}
	
	public static void main(String[] args) {			    
		MarkovChain mc = new MarkovChain();         
		String sentence1 = "CIS 120 rocks";
		String sentence2 = "CIS 120 beats CIS 160";
        mc.train(Arrays.stream(sentence1.split(" ")).iterator());
        mc.train(Arrays.stream(sentence2.split(" ")).iterator());
        System.out.println(mc.chain);	        
        mc.reset("CIS"); 
        while(mc.hasNext()) {
        	System.out.println(mc.next());
        }
	}
}
