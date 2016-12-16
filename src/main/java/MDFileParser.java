/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MDFileParser {

  private Pattern pattern = Pattern.compile("['|\"](.*?)['|\"]");

  public List<MDFileContent> parse(String rootDir, List<String> filePaths) {
    List<MDFileContent> mdFiles = new LinkedList<>();
    for (String filePath : filePaths) {
      MDFileContent mdFile = null;
      try {
        mdFile = parse(filePath, rootDir);
        mdFiles.add(mdFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return mdFiles;
  }

  private MDFileContent parse(String filePath, String rootDir) throws IOException {
    File file = new File(filePath);
    String content = readFile(filePath, StandardCharsets.UTF_8);
    int headEndIdx = getHeaderEndIdx(content);
    String header = content.substring(0, headEndIdx);
    String title = extractTitle(header).trim();

    String body = content.substring(headEndIdx);
    String imgPath = extractPathOfFirstImage(body);

    MDFileContent fileContent = new MDFileContent();
    fileContent.body = filterMDLanguage(body);
    fileContent.title = title;
    fileContent.fileName = file.getName();
    fileContent.computePathFromRootDir(rootDir, filePath);
    fileContent.computeImgPathFromRootDir(imgPath);

    return fileContent;
  }

  private String filterMDLanguage(String text) {
    text = removeLineBreaks(text);
    text = removeMultipleWhitespaces(text);
    text = removeHashTags(text);
    text = removeAsterisk(text);
    text = removeCodeSamples(text);
    text = removeCurlyBracesContent(text);
    text = removeSquareBrackets(text);
    text = removeGraveAccent(text);
    text = removeParenthesesWithoutContent(text);
    return text;
  }

  private String removeLineBreaks(String text) {
    return text.replaceAll("[\\t\\n\\r]"," ");
  }

  private String removeMultipleWhitespaces(String text){
    return text.trim().replaceAll(" +", " ");
  }

  private String removeHashTags(String text) {
    return text.replaceAll("#", "");
  }

  private String removeAsterisk(String text) {
    return text.replaceAll("\\*", "");
  }

  private String removeCodeSamples(String text) {
    return text.replaceAll("```.*?```", "");
  }

  private String removeCurlyBracesContent(String text) {
    text = text.replaceAll("\\{\\{\\{.*?\\}\\}\\}", "");
    text = text.replaceAll("\\{\\{.*?\\}\\}", "");
    text = text.replaceAll("\\{.*?\\}", "");
    return text;
  }

  private String removeSquareBrackets(String text) {
    return text.replaceAll("\\[|\\]", "");
  }

  private String removeGraveAccent(String text) {
    return text.replaceAll("`", "");
  }

  private String removeParenthesesWithoutContent(String text) {
    return text.replaceAll("\\( *\\)", "");
  }

  private String readFile(String path, Charset encoding) throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, encoding);
  }

  private String extractPathOfFirstImage(String content) {
    int pathIdx = content.indexOf("img src");
    if (pathIdx > 0) {
      String textFromImgSrcToken = content.substring(pathIdx);
      return getTextBetweenQuotes(textFromImgSrcToken);
    } else {
      return "";
    }
  }

  private String extractTitle(String header) {
    int titleIdx = header.indexOf("title");
    String textFromTitleToken = header.substring(titleIdx);
    return getTextBetweenQuotes(textFromTitleToken);
  }

  private String getTextBetweenQuotes(String textWithQuotes) {
    Matcher matcher = pattern.matcher(textWithQuotes);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return "";
  }

  private int getHeaderEndIdx(String fileContent) {
    String headerStartEnd = "---";
    int firstHyphens = fileContent.indexOf(headerStartEnd);
    int idxOfLastHyphens = fileContent.indexOf(headerStartEnd, skipHyphens(firstHyphens));
    return skipHyphens(idxOfLastHyphens);
  }

  private int skipHyphens(int index) {
    return index + 3;
  }
}
