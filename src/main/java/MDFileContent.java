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

public class MDFileContent {

  public String pathFromRootDir;
  public String fileName;
  public String imgPathFromRootDir;

//  public MDFileHeader header;

  public String title;
  public String body;

  public void computePathFromRootDir(String rootPath, String filePath) {
    String pathDiff = computePathDiff(rootPath, filePath);
    if(pathDiff.endsWith("index.md")){
      pathDiff = pathDiff.replaceAll("index.md", "");
    }else{
      pathDiff = pathDiff.replaceAll(".md", "/");
    }
    this.pathFromRootDir = pathDiff;
  }

  private String computePathDiff(String rootPath, String filePath) {
    String pathDiff = "";
    if (rootPath.length() > filePath.length()) {
      pathDiff =  rootPath.substring(filePath.length() - 1);
    } else if (filePath.length() > rootPath.length()) {
      pathDiff = filePath.substring(rootPath.length() - 1);
    }
    return pathDiff;
  }

  public void computeImgPathFromRootDir(String imgPath) {
    String imgPathFromRootDir = "";
    if(imgPath.startsWith("../")){
      int idxFromLastPath = pathFromRootDir.lastIndexOf("/");
      String relativeStart = pathFromRootDir.substring(0, idxFromLastPath);
      String imgPathReturnSkipped = imgPath.substring(2);
      imgPathFromRootDir = relativeStart + imgPathReturnSkipped;
    }
    this.imgPathFromRootDir = imgPathFromRootDir;
  }
}
