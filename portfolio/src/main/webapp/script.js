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
 * Adds a random quote to the page.
 */
function addRandomQuote() {
    const quotes =
        ['My "people skills" are "rusty"', "What would Hermione do?", "It matters not what someone is born, but what they grow to be.",
            "And if eating cake is wrong, I don't want to be right."];
    const authors =
        ["Castiel, Supernatural", "Charlie Bradbury, Supernatural", "Albus Dumbledore", "Lorelai Gilmore, Gilmore Girls"];

    //Pick random quote
    const randomIndex = Math.floor(Math.random() * quotes.length);
    const quote = quotes[randomIndex];
    const author = authors[randomIndex];

    //Add it to the page.
    const quoteText = document.getElementById('quote-text');
    quoteText.innerText = '"' + quote + '"';

    const quoteAuthor = document.getElementById('quote-author');
    quoteAuthor.innerText = "- " + author;
}

function deleteComments() {
    const request = new Request('/delete-data', { method: 'POST' });

    fetch(request).then(getComments());
}
function getComments() {
    requestLimitSelect = document.getElementById("max-comments-fetched");
    requestLimit = requestLimitSelect.options[requestLimitSelect.selectedIndex].text;

    languageDropdown = document.getElementById("language-dropdown");
    selectedLanguage = languageDropdown.value;

    fetchURL = "/data?request-limit=" + requestLimit + "&language=" + selectedLanguage;
    fetch(fetchURL).then(response => response.json()).then((comments) => {
        commentsContainer = document.getElementById("comments-container");
        commentsContainer.innerHTML = '';
        for (i = 0; i < comments.length; i++) {
            commentsContainer.appendChild(
                createCommentElement(comments[i].text, comments[i].author, selectedLanguage));
        }
    });
}

function createCommentElement(text, author, langCode) {
    const textElement = document.createElement("p");
    textElement.innerText = text;
    textElement.setAttribute("lang", langCode);
    const authorElement = document.createElement('div');
    authorElement.innerText = author;

    const commentElement = document.createElement('div');
    commentElement.setAttribute("class", "comment");
    commentElement.appendChild(textElement);
    commentElement.appendChild(authorElement);

    return commentElement;
}
/** Creates an <li> element containing text. */
function createListElement(text) {
    const liElement = document.createElement('li');
    liElement.innerText = text;
    return liElement;
}

function loadLanguageDropdown() {
    dropdown = document.getElementById("language-dropdown");
    dropdown.innerHTML = '';
    fetch("/provided-languages").then(response => response.json()).then((languages) => {
        for (i = 0; i < languages.length; i++) {
            dropdown.appendChild(
                createOptionElement(languages[i].name, languages[i].code)
            );
        }
    })
}

function createOptionElement(lang, code) {
    optionElement = document.createElement("option");
    optionElement.setAttribute("value", code);
    optionElement.innerHTML = lang;

    return optionElement;
}

function loadPage() {
    getComments();
    loadLanguageDropdown();
}

let map;
function initMap() {
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: -35.28346, lng: 149.12807 },
        zoom: 9,
    });
}