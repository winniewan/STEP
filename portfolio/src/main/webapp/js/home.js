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

    const loginElement = document.getElementById('login-button');
    loginElement.appendChild(createUrlElement(user.url, user.isLoggedin));
  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}

function createUrlElement(link, isLoggedin) {    
    const aElement = document.createElement('a');
    aElement.setAttribute('href', link);
    if (isLoggedin) {
      aElement.innerText = "Logout";
    } else {
      aElement.innerText = "Login";
    }
    return aElement;
}
