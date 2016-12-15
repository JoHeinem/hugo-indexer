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
import java.util.LinkedList;
import java.util.List;

public class MDFileFinder {

  public List<String> searchDirectory(String rootDir) {
    List<String> list = new LinkedList<>();
    search(list, rootDir);
    return list;
  }

  private void search(List<String> list, String dir) {
    File root = new File(dir);
    File[] files = root.listFiles();

    if (files == null) return;

    for (File f : files) {
      if (f.isDirectory()) {
        search(list, f.getAbsolutePath());
      } else if (f.isFile() && f.getName().endsWith(".md")) {
          list.add(f.getPath());
      }
    }
  }
}
