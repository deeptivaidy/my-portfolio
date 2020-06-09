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

async function getComment() {
    const response = await fetch('/data');
    const comment = await response.text();
    document.getElementById('comment-container').innerText=comment;
}
