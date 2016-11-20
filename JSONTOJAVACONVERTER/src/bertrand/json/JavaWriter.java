package bertrand.json;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import bertrand.common.StringUtils;
import bertrand.json.Node.BELONG;
import bertrand.json.Node.TYPE;

public class JavaWriter {
	
	private List<Node> nodeList;
	private String fileName;
	private String path;
	private String packageName;
	
	private int curIndex = 0;
	private Set<String> importSet = new HashSet<String>();
	private StringBuilder headerBuilder = new StringBuilder();
	private StringBuilder[] bodyBuilder = new StringBuilder[100];
	
	public void setFileName(String fileName) {
		this.fileName = StringUtils.toUpperCaseFirstOne(fileName);
	}
	
	public void setPath(String path) {
		this.path = path;
		if(!path.endsWith(File.separator)) {
			this.path = path + File.separator;
		}
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
		
	}
	
	public JavaWriter(List<Node> nodeList) {
		this.nodeList = nodeList;
		this.curIndex = 0;
	}
	
	public void gen() {
		try {
			String name = getFileName();
			System.out.println(name);
			File file = new File(name);
			FileWriter writer = new FileWriter(file);
			bodyBuilder[0] = new StringBuilder();
			headerBuilder.append(JavaToken.PAGKAGE + packageName + ";" + JavaToken.NEWLINE);
			bodyBuilder[0].append(JavaToken.PUBLIC + JavaToken.CLASS + fileName + " {" + JavaToken.NEWLINE);
			write(0);
			bodyBuilder[0].append("}");
			buildImport(headerBuilder);
			writer.write(headerBuilder.toString());
			writer.write(bodyBuilder[0].toString());
			writer.flush();
			writer.close();
			JavaFomater.format(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getFileName() {
		StringBuilder sb = new StringBuilder();
		if(path != null) {
			sb.append(path);
		}
		if(packageName != null) {
			String[] pathInfo = packageName.split("\\.");
			for (String string : pathInfo) {
				sb.append(string).append(File.separator);
			}
		}
		return sb.append(fileName + ".java").toString();
	}
	
	private String getFileName(String fileName1) {
		StringBuilder sb = new StringBuilder();
		if(path != null) {
			sb.append(path);
		}
		if(packageName != null) {
			String[] pathInfo = packageName.split("\\.");
			for (String string : pathInfo) {
				sb.append(string).append(File.separator);
			}
		}
		return sb.append( fileName1+ ".java").toString();
	}
	
	private void write(int depth) {
			while(curIndex < nodeList.size()) {
				Node node = nodeList.get(curIndex);
				int nowDepth = node.getDepth();
				if(nowDepth < depth) {
					break;
				}
				++curIndex;
				String name = node.getName();
				TYPE type = node.getType();
				if(type == TYPE.OBJECT) {
					String name1 = getFileName( StringUtils.toUpperCaseFirstOne(name));
					System.out.println(name1);
					File file = new File(name1);
					try {
						FileWriter writer = new FileWriter(file);
					
					bodyBuilder[depth+ 1] = new StringBuilder();
					bodyBuilder[depth].append(JavaToken.PUBLIC + StringUtils.toUpperCaseFirstOne(name) + " " + name + ";" + JavaToken.NEWLINE);
					bodyBuilder[depth+ 1].append(
							JavaToken.PUBLIC + JavaToken.CLASS + StringUtils.toUpperCaseFirstOne(name) + " {");
					write(depth + 1);
					bodyBuilder[depth+ 1].append("}");
					buildImport(headerBuilder);
					writer.write(headerBuilder.toString());
					writer.write(bodyBuilder[depth+ 1].toString());
					writer.flush();
					writer.close();
					JavaFomater.format(file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(type == TYPE.LIST) {
					addImport(type);
					writeList(depth + 1, name);
				}else {
					bodyBuilder[depth].append(JavaToken.PUBLIC + type.getName() + " " + name + ";" + JavaToken.NEWLINE);
				}
			}
//			bodyBuilder.append("}");
	}
	
	private void writeList(int depth, String listName) {
		if(curIndex < nodeList.size()) {
			Node node = nodeList.get(curIndex);
			int nowDepth = node.getDepth();
			if(nowDepth < depth) {
				return;
			}
			TYPE type = node.getType();
			BELONG belong = node.getBelong();
			if(TYPE.LIST == type) {
				throw new IllegalStateException("Waiting to support...");
			}else if(TYPE.OBJECT == type || BELONG.OBJECT == belong) {
				String name1 = getFileName( StringUtils.toUpperCaseFirstOne(listName));
				System.out.println(name1+depth);
				File file = new File(name1);
				try {
					FileWriter writer = new FileWriter(file);
				
				bodyBuilder[depth+ 1] = new StringBuilder();
				
				bodyBuilder[depth-1].append(JavaToken.PUBLIC + "List<" + StringUtils.toUpperCaseFirstOne(listName) + "> " + listName + ";" + JavaToken.NEWLINE);
				bodyBuilder[depth+1].append(
						JavaToken.PUBLIC +  JavaToken.CLASS + StringUtils.toUpperCaseFirstOne(listName) + " {");
				write(depth + 1);
				bodyBuilder[depth+1].append("}");
				buildImport(headerBuilder);
				writer.write(headerBuilder.toString());
				writer.write(bodyBuilder[depth+ 1].toString());
				writer.flush();
				writer.close();
				JavaFomater.format(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				bodyBuilder[depth].append(JavaToken.PUBLIC + "List<" + type.getName() + ">" + " name" + ";" + JavaToken.NEWLINE);
				++curIndex;
			}
		}
	}
	
	private void addImport(TYPE type) {
		if(type == TYPE.LIST) {
			importSet.add(ImportPackage.LIST);
		}
	}
	
	private void buildImport(StringBuilder headerBuilder) {
		Iterator<String> iterator = importSet.iterator();
		while(iterator.hasNext()) {
			headerBuilder.append("import ").append(iterator.next()).append(";");
		}
	}
	
	
}
