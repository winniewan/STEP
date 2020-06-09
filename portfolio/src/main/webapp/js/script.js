/** Displays a list of comments from user's preference */
function displayComments() {
  const pageSize = document.getElementById('page-size').value;

  const commentElement = document.getElementById('comment-section');
  commentElement.innerHTML = '';

  fetch('/comment?page-size='+pageSize)
    .then(response => response.json())
    .then((comments) => comments.forEach((comment) => {
      commentElement.appendChild(createCommentElement(comment));
    }));
  
  fetch('/login').then(response => response.json()).then((user) => {
    displayForm("comment-container", user.isLoggedin, commentElement);
  });
}

/** Creates an element that represents a comment. */
function createCommentElement(comment) {
  const commentElement = document.createElement('li');
  commentElement.className = 'comment';

  const textElement = document.createElement('span');
  textElement.innerText = comment.text;

  const deleteButtonElement = document.createElement('button');
  deleteButtonElement.innerText = 'Delete';
  deleteButtonElement.addEventListener('click', () => {
    deleteComment(comment);

    // Remove the comment from the DOM.
    commentElement.remove();
  });

  commentElement.appendChild(textElement);
  commentElement.appendChild(deleteButtonElement);
  return commentElement;
}

/** Tells the server to delete the comment. */
function deleteComment(comment) {
  fetch('/comment/'+comment.id, {method:"DELETE"}); 
}

/** Decides whether to hide or show comment form. */
function displayForm(form, isLoggedin, commentElement) {
  if(isLoggedin) {
    document.getElementById(form).hidden = false;
  } else {
    document.getElementById(form).hidden = true;
    commentElement.innerHTML = 'Please login';
  }
}
