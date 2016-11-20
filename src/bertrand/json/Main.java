package bertrand.json;

public class Main {

	public static void main(String[] args) {
		NodeListGen gen = new NodeListGen();
		gen.gen("test.json");
		JavaWriter writer = new JavaWriter(gen.getNodeList());
		writer.setFileName("test");
		writer.setPath("src/");
		writer.setPackageName("dest.bertrand.json");
		writer.gen();
	}
}
