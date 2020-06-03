/** Displays a list of comments from user's preference */
function displayComments() {
  const pageSize = document.getElementById('page-size').value;

  const commentElement = document.getElementById('comment-section');
  commentElement.innerHTML = '';

  fetch('/comments?page-size='+pageSize).then(response => response.json()).then((comments) => comments.forEach((comment) => {
    commentElement.appendChild(createCommentElement(comment));
  }))
};

/** Creates an element that represents a comment. */
function createCommentElement(comment) {
  const commentElement = document.createElement('li');
  commentElement.className = 'comment';

  const textElement = document.createElement('span');
  textElement.innerText = comment.text;

  commentElement.appendChild(textElement);
  return commentElement;
}
