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

    const natureWalks = [
        {
            position: { lat: -35.2916, lng: 149.0898 },
            map: map,
            title: 'Weston Park'
        },
        {
            position: { lat: -35.2789, lng: 149.1089 },
            map: map,
            title: 'Australian National Botanic Gardens'
        },
        {
            position: { lat: -35.5313, lng: 149.0657 },
            map: map,
            title: 'Namadgi National Park'
        },
        {
            position: { lat: -35.2985, lng: 149.1418 },
            map: map,
            title: 'Lake Burley Griffin: Bridge to Bridge'
        },
        {
            position: { lat: -35.27000, lng: 149.15833 },
            map: map,
            title: 'Mount Ainslie Summit Trail'
        },
        {
            position: { lat: -35.1421, lng: 149.0915 },
            map: map,
            title: 'One Tree Hill Summit'
        },
        {
            position: { lat: -35.2650, lng: 149.0678 },
            map: map,
            title: 'Mount Painter Summit Walk'
        },
        {
            position: { lat: -35.4442, lng: 148.8920 },
            map: map,
            title: 'Tidbinbilla Nature Reserve'
        },
        {
            position: { lat: -35.2736, lng: 149.0975 },
            map: map,
            title: 'Black Mountain'
        },
        {
            position: { lat: -35.39553306, lng: 149.0101326 },
            map: map,
            title: 'Murrumbidgee Discovery Track'
        }
    ]

    // Create markers.
    for (walk of natureWalks) {
        const marker = new google.maps.Marker({
            position: walk.position,
            title: walk.title,
            map: map,
        });

        const infowindow = new google.maps.InfoWindow({
            content: marker.title,
        });

        marker.addListener("click", () => {
            infowindow.open(map, marker);
        });
    }

}