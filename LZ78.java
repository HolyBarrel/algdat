package HEight;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class LZ78 {

  public static class LZ78Compression {

    private ArrayList<String> dictionary = new ArrayList<>();
    private ArrayList<String> encodings = new ArrayList<>();
    private byte[] bytes;

    //private static byte[] data;

    public void readFileToBytes() {
      Path path = Paths.get("src/HEight/test.txt");
      byte[] data = new byte[0];
      try {
        data = Files.readAllBytes(path);
      } catch (IOException e) {
        e.printStackTrace();

      }
      bytes = data;
    }


    public void findUniquePhrases() {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < bytes.length; i++) {
        sb.append(bytes[i]);
        if (!dictionary.contains(sb.toString())) {
          dictionary.add(sb.toString());
          sb = new StringBuilder();
        }
      }
      if (!sb.isEmpty()) {
        dictionary.add(sb.toString());
      }
    }

    public void phraseToEncodingFinal() {
      encodings.add(0, "|" + dictionary.get(0));
      for (int i = 1; i < dictionary.size(); i++) {
        String phrase = dictionary.get(i);
        boolean foundEncoding = false;

        for (int j = 1; j < phrase.length() - 1; j++) {
          String temp = phrase.substring(0, phrase.length() - (j));
          if (dictionary.contains(temp)) {
            String index = String.valueOf(dictionary.indexOf(temp));
            String encoding;
            if (index.contains("|")){
              encoding = dictionary.indexOf(index) + "|" + phrase.substring(temp.length());
            } else {
              encoding = dictionary.indexOf(temp) + "|" + phrase.substring(temp.length());
            }
            if (!encodings.contains(encoding)) {
              encodings.add(encoding);
            }
            foundEncoding = true;
            break;
          }
        }
        if (!foundEncoding) {
          encodings.add("|" + phrase);
        }
      }
    }

    public void encodingsToCompressedFile() throws IOException {
      StringBuilder stringBuilder = new StringBuilder();
      for (String encoding : encodings) {
        stringBuilder.append(encoding).append(",");
      }
      File file = new File("src/HEight/test2.txt");
      byte[] data = new byte[stringBuilder.length()];
      for (int i = 0; i < stringBuilder.length(); i++){
        data[i] = (byte) stringBuilder.charAt(i);
      }
      data = stringBuilder.toString().getBytes();

      Files.write(file.toPath(), data);
    }

    public ArrayList<String> getDictionary() {
      return dictionary;
    }

    public byte[] getBytes() {
      return bytes;
    }

    public ArrayList<String> getEncodings() {
      return encodings;
    }
  }

  public class LZ78Decompression{
    private ArrayList<String> dictionary = new ArrayList<>();
    private ArrayList<String> encodings = new ArrayList<>();





  }
  public static void main(String[] args) throws IOException {
    LZ78Compression lz78 = new LZ78Compression();
    lz78.readFileToBytes();
    lz78.findUniquePhrases();
    lz78.phraseToEncodingFinal();
    ArrayList<String> strings = lz78.getDictionary();
    for (String s : strings){
      System.out.println(s);
    }
    System.out.println("----------------------");
    ArrayList<String> encodings = lz78.getEncodings();
    for (int i = 0; i < encodings.size(); i++){
      System.out.println(i + ".    " + encodings.get(i));
    }
    System.out.println("----------------------");

    System.out.println(lz78.getBytes().length);
    System.out.println(lz78.getDictionary().size());
    System.out.println(lz78.getEncodings().size());
    System.out.println("----------------------");

    lz78.encodingsToCompressedFile();
  }
}