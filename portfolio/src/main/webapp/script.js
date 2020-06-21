// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ["\"Don't stress, don't worry, work hard, eat curry!\"", '\"If you compete with others you become bitter. But if you compete with yourself you become better.\"', "\"A ship in port is safe, but that's not what ships are built for.\"", "\"There's a silver lining in every dark moment, a lesson to be learned from every failure if you just pay attention.\""];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

/**
 * Gets a comment from the server and creates a new div to display 
 * if it does not already exist.
 */
async function getComment() {
    if (document.getElementById("comment-container")) {
        console.log("comment container exists")
    } else {
        document.getElementById("container").style.display = "none";
        //Creates and formats a div that shows the content
        var login = await fetch('/login');
        var result = await login.json();

        //Checks if user is logged in
        if (result[0].length > 0) {
            document.getElementById("container").style.display = "block";
            document.getElementById("container").appendChild(
                document.createElement('button')).innerHTML
                 = "<a href=\"" + result[1] + "\">Logout</a>";
            var d = document.createElement("div");
            const response = await fetch('/data');
            const comments = await response.json()
                .then(comment => {
                    for(const m of comment) {
                        let para = document.createElement('div');
                        let message = "\"" + m.content + "\" - <i>" + m.author + "</i>";
                        para.appendChild(document.
                            createElement('strong')).innerHTML = message;
                        d.appendChild(para);
                    }
                }
            );
            d.className="content";
            d.id="comment-container"
            document.body.appendChild(d);
            
        } else {
            var d = document.createElement("div");
            d.innerHTML = "<button onclick=\"location.href='"+ result[1]+"'\">Login!</button>";
            d.className="content";
            d.id="comment-container"
            document.body.appendChild(d);
        }
        
    }
}
