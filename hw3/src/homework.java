import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class homework 
{
	public static int count = 1;
	static class predicate
	{
		String name;
		boolean neg;
		String[] argument;
		boolean[] isConstant;
		public void turn()
		{
			if(neg) neg = false;
			else neg = true;
		}
		public String toString()
		{
			StringBuffer sb = new StringBuffer();
			if(neg) sb.append("~");
			sb.append(this.name);
			sb.append("(");
			for(int i = 0; i < argument.length; i++) 
			{
				sb.append(argument[i]);
				if(i != argument.length-1) sb.append(",");
			}
			
			sb.append(")");
			return sb.toString();
		}
		predicate(String s)
		{
			name = s.split("\\(")[0]; 
			if(name.charAt(0) == '~') 
			{
				neg = true;
				name = name.substring(1);
			}
			
			String arguments = s.split("\\(")[1];
			arguments = arguments.substring(0, arguments.length()-1);

			argument = arguments.split(",");
			isConstant = new boolean[argument.length];
			for(int i = 0; i < argument.length; i++)
			{
				//System.out.println(argument[i]);
				if(argument[i].charAt(0) >= 'A' && argument[i].charAt(0) <= 'Z')
				{
					isConstant[i] = true;
				}
			}
		}
		predicate(String s, int num)
		{
			name = s.split("\\(")[0]; 
			if(name.charAt(0) == '~') 
			{
				neg = true;
				name = name.substring(1);
			}
			
			String arguments = s.split("\\(")[1];
			arguments = arguments.substring(0, arguments.length()-1);

			argument = arguments.split(",");
			isConstant = new boolean[argument.length];
			for(int i = 0; i < argument.length; i++)
			{
				//System.out.println(argument[i]);
				if(argument[i].charAt(0) >= 'A' && argument[i].charAt(0) <= 'Z')
				{
					isConstant[i] = true;
				}
				else
				{
					argument[i] += String.valueOf(num);
				}
			}
		}
		
		
		
		
	}
	
	static class rule
	{
		List<predicate> predicates;
		int size;
		
		public rule(List<predicate> list)
		{
			predicates = new ArrayList<predicate>();
			for(int i = 0; i < list.size(); i++)
			{
				predicates.add(list.get(i));
			}
			size = list.size();
		}
		
		public rule()
		{
			predicates = new ArrayList<predicate>();
			size = 0;
		}
		public rule(String str, int num)
		{
			String[] ps = str.split("\\|");
			predicates = new ArrayList<predicate>();
			for(int i = 0; i < ps.length; i++)
			{
				//System.out.println(ps[i].trim());
				predicate p = new predicate(ps[i].trim(), num);
				predicates.add(p);
			}
			size = ps.length;
		}
		
		public String toString()
		{
			StringBuffer sb = new StringBuffer();
			for(int j = 0; j < predicates.size(); j++)
			{
				predicate p = predicates.get(j);
				sb.append(p.toString());
				if(j != predicates.size()-1) sb.append(" | ");
			}
			return sb.toString();
		}
		
		public boolean equals(rule r)
		{
			return r.toString().equals(this.toString());
		}
	}
	
	
	
	public static boolean isOpposite(predicate p1, predicate p2)
	{
		if(!p1.name.equals(p2.name))	return false;
		if(p1.neg == p2.neg) return false;
		if(p1.argument.length != p2.argument.length) return false;
		for(int i = 0; i < p1.argument.length; i++)
		{
			if(p1.isConstant[i] && p2.isConstant[i])
			{
				if(!p1.argument[i].equals(p2.argument[i]))	return false;
				else continue;
			}
			if(!p1.isConstant[i] && !p2.isConstant[i])
			{
				continue;
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	
	
	public static boolean isContradict(rule rule1, rule rule2)
	{
		if(rule1.size != 1) return false;
		if(rule2.size != 1) return false;
		
		if(!isOpposite(rule1.predicates.get(0), rule2.predicates.get(0)))
		{
			return false;
		}
		
		System.out.println("Contradict!");
		System.out.println(rule1);
		System.out.println(rule2);
		System.out.println("__________");
		return true;
	}
	
	public static void replaceAll(List<predicate>list, String var, String cst, boolean isvar)
	{
		for(int i = 0; i < list.size(); i++)
		{
			predicate p = list.get(i);
			for(int j = 0; j < p.argument.length; j++)
			{
				if(p.argument[j].equals(var))
				{
					p.argument[j] = cst;
					if(!isvar)	p.isConstant[j] = true;
				}
			}
		}
	}
	
	public static void unify(List<predicate> list1, List<predicate> list2)
	{
		for(int i = 0; i < list1.size(); i++)
		{
			for(int j = 0; j < list2.size(); j++)
			{
				predicate p1 = list1.get(i);
				predicate p2 = list2.get(j);
				if(p1.name.equals(p2.name) && p1.argument.length == p2.argument.length)
				{
					for(int k = 0; k < p1.argument.length ; k++)
					{
						if(p1.isConstant[k] && !p2.isConstant[k])
						{
							String var = p2.argument[k];
							String cst = p1.argument[k];
							replaceAll(list2, var, cst, false);
						}
						if(!p1.isConstant[k] && p2.isConstant[k])
						{
							String var = p1.argument[k];
							String cst = p2.argument[k];
							replaceAll(list1, var, cst, false);
						}
					}
				}
			}
		}
	}
	
	public static void normalize(predicate p1, predicate p2, List<predicate> list2)
	{
		for(int k = 0; k < p1.argument.length ; k++)
		{
			if(!p1.isConstant[k] && !p2.isConstant[k])
			{
				//p2.argument[k] -> p1.argument[k]
				String var1 = p1.argument[k];
				String var2 = p2.argument[k];
				replaceAll(list2, var2, var1, true);
			}
		}
				

	}
	
	public static rule infer(rule rule1, rule rule2)
	{
		boolean flag = false;
		List<predicate> list1 = new ArrayList<predicate>();
		List<predicate> list2 = new ArrayList<predicate>();
		for(predicate p:rule1.predicates)	
		{
			predicate np = new predicate(p.toString());
			list1.add(np);
		}
		for(predicate p:rule2.predicates)
		{
			predicate np = new predicate(p.toString());
			list2.add(np);
		}
		
		
		unify(list1, list2);
		for(int i = 0; i < list1.size(); i++)
		{
			for(int j = 0; j < list2.size(); j++)
			{
				if(isOpposite(list1.get(i), list2.get(j)))
				{
					predicate p1 = list1.get(i);
					predicate p2 = list2.get(j);
					normalize(p1, p2, list2);
					list1.remove(i);
					list2.remove(j);
					i--;
					j--;
					flag = true;
					break;
				}
			}
		}
		
		if(!flag)	return new rule();
		
		List<predicate> list = new ArrayList<predicate>();
		HashMap<String, Integer>map = new HashMap<String, Integer>();
		
		for(predicate p:list1) 
		{
			if(!map.containsKey(p.toString()))
			{
				map.put(p.toString(), 1);
				list.add(p);
			}
		}
		for(predicate p:list2) 
		{
			if(!map.containsKey(p.toString()))
			{
				map.put(p.toString(), 1);
				list.add(p);
			}
			
		}
		return new rule(list);
	}

	public static boolean inferContradiction(List<rule> KBQ)
	{
		System.out.println("KB");
		for(int i = 0; i < KBQ.size(); i++)
		{
			System.out.println(KBQ.get(i));
		}
		System.out.println("_________start_infer_________");
		HashMap<String, Integer> rule_map = new HashMap<String, Integer>();
		for(rule r:KBQ) 
		{
			rule_map.put(r.toString(), 1);
		}
		List<rule> buff = new ArrayList<rule>();
		for(int i = 0; i < KBQ.size(); i++)
		{
			for(int j = i+1; j < KBQ.size(); j++)
			{
				if(isContradict(KBQ.get(i), KBQ.get(j))) return true;
				rule new_rule = infer(KBQ.get(i), KBQ.get(j));
				if(new_rule.size > 0 && !rule_map.containsKey(new_rule.toString())) 
				{
					System.out.println(">>>   "+KBQ.get(i).toString() + "  +  "+ KBQ.get(j).toString() +"---->");
					System.out.println(new_rule.toString());
					rule_map.put(new_rule.toString(), 1);
					buff.add(new_rule);
				}
			}
		}
		
		int count = 1;
		while(buff.size() > 0)
		{
			System.out.println(count++);
			//for(rule r:buff) System.out.println(r);
			List<rule> tmp = new ArrayList<rule>();
			for(int i = 0; i < buff.size(); i++)
			{
				for(int j = 0; j < buff.size(); j++)
				{
					if(isContradict(buff.get(i), buff.get(j))) return true;
					rule new_rule = infer(buff.get(i), buff.get(j));
					if(new_rule.size > 0 && !rule_map.containsKey(new_rule.toString())) 
					{
						System.out.println(">>>   "+buff.get(i).toString() + "  +  "+ buff.get(j).toString() +"---->");
						System.out.println(new_rule.toString());
						rule_map.put(new_rule.toString(), 1);
						tmp.add(new_rule);
					}
				}
			}
			for(int i = 0; i < KBQ.size(); i++)
			{
				for(int j = 0; j < buff.size(); j++)
				{
					if(isContradict(KBQ.get(i), buff.get(j))) return true;
					rule new_rule = infer(KBQ.get(i), buff.get(j));
					if(new_rule.size > 0 && !rule_map.containsKey(new_rule.toString())) 
					{
						System.out.println(">>>   "+KBQ.get(i).toString() + "  +  "+ buff.get(j).toString() +"---->");
						System.out.println(new_rule.toString());
						rule_map.put(new_rule.toString(), 1);
						tmp.add(new_rule);
					}
				}
			}
			
			
			for(rule r:buff)
			{
				KBQ.add(r);
			}
			
//			System.out.println("KB");
//			for(int i = 0; i < KBQ.size(); i++)
//			{
//				System.out.println(KBQ.get(i));
//			}
			buff = tmp;
		}
		
		
		
		return false;
	
	}
	
	
	public static boolean resolution(rule query, List<rule> KB)
	{
		List<rule> KBQ = new ArrayList<rule>();
		for(int i = 0; i < KB.size(); i++)
		{
			KBQ.add(KB.get(i));
		}
		for(int i = 0; i < query.size; i++)
		{
			predicate q = query.predicates.get(i);
			q.turn();
		}
		KBQ.add(query);
		return inferContradiction(KBQ);
	}
	
	
	//First-order logic resolution
	public static boolean[] doQuery(String[] queries, String[] sentences)
	{
		boolean[] res = new boolean[queries.length];
		List<rule> KB = new ArrayList<rule>();
		for(int i = 0; i < sentences.length; i++)
		{
			//System.out.println(sentences[i]);
			rule sentence = new rule(sentences[i], count++);
			KB.add(sentence);
		}
		for(int i = 0; i < queries.length; i++)
		{
			rule query = new rule(queries[i], count++);
			res[i] = resolution(query, KB);
		}
		
		return res;
	}
	
	public static void main(String[] args)
	{
		
		int query_number = 0;
		int sentence_number = 0;
		String[] queries = null;
		String[] sentences = null;
		try 
		{
			File file_in = new File("input.txt");
			FileReader fr = new FileReader(file_in);
			BufferedReader br = new BufferedReader(fr);
			String s = br.readLine().trim();
			query_number = Integer.parseInt(s);
			queries = new String[query_number];
			for(int i = 0; i < query_number; i++)
			{
				s = br.readLine().trim();
				queries[i] = s;
			}
			s = br.readLine().trim();
			sentence_number = Integer.parseInt(s);
			sentences = new String[sentence_number];
			for(int i = 0; i < sentence_number; i++)
			{
				s = br.readLine().trim();
				sentences[i] = s;
			}
			br.close();
			fr.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		

		boolean[] res = doQuery(queries, sentences);
		
		
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < res.length; i++)
		{
			if(res[i]) sb.append("TRUE");
			else sb.append("FALSE");
			if(i != res.length-1) sb.append("\r\n");
		}
		System.out.println();
		System.out.println("Result:");
		System.out.println(sb.toString());
		try
		{
			File file_out = new File("output.txt");
			FileWriter fw = new FileWriter(file_out);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sb.toString());
			bw.close();
			fw.close();
			
		}catch(IOException e)
		{
			e.printStackTrace();
		}

		
	}
}
