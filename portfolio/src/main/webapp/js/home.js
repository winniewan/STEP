/** Update log-in status of users */
function getLoginStatus() {
  fetch('/login').then(response => response.json()).then((user) => {
    console.log(user);
    const statusElement = document.getElementById('status-container');
    statusElement.innerHTML = '';
    statusElement.appendChild(
        createListElement('Is the user logged in: ' + user.isLoggedin));
    statusElement.appendChild(
        createListElement('User email: ' + user.email));
    statusElement.appendChild(
        createLinkElement(user.url));
  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}

function createLinkElement(link) {
    const liElement = document.createElement('li');
    
    const aElement = document.createElement('a');
    aElement.setAttribute('href', link);

    liElement.innerText = "URL: ";
    liElement.appendChild(aElement);
    aElement.innerText = "Click here";
    return liElement;
}
