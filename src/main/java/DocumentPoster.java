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

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;


public class DocumentPoster {

  TransportClient client;

//  public DocumentPoster(String inetAddress, int port){
//    this.inetAddress = inetAddress;
//    this.port = port;
//  }

  public DocumentPoster(){
    try {
      client = new PreBuiltTransportClient(Settings.EMPTY)
          .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  public void post(List<MDFileContent> documents) {
    for (MDFileContent document : documents) {
      post(document);
    }
  }

  private void post(MDFileContent doc) {
    try {
      client.prepareIndex("camunda", "docs")
          .setSource(jsonBuilder()
              .startObject()
              .field("title", doc.title)
              .field("body", doc.body)
              .field("filename", doc.fileName)
              .field("pathFromRoot", doc.pathFromRootDir)
              .field("imgPathFromRoot", doc.imgPathFromRootDir)
              .endObject()
          )
          .get();
    } catch (Exception e) {
      shutdown();
      e.printStackTrace();
    }
  }

  public void shutdown(){
    client.close();
  }
}
