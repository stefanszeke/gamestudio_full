const commentsTable = document.getElementById('comments-table');
const gameSelect = document.getElementById('gameSelect');

const addForm = document.getElementById('addForm');
const patchForm = document.getElementById('patchForm');

const testButton = document.querySelector('.test');

const getCommentsButton = document.getElementById('getCommentsButton');

let selectedGame = '';
let selectedId = '';

// set selected game
gameSelect.addEventListener('change', (event) => {
    selectedGame = event.target.value;
});

// get comments button
getCommentsButton.addEventListener('click', (e) => {
    selectedGame = gameSelect.value;
    getComments();
});


// test
testButton.addEventListener('click', (e) => {
  console.log("game: " + addForm[0].value);
  console.log("game: " + addForm[1].value);
  console.log("game: " + addForm[2].value);
});

// select id by clicking on a row
async function selectComment(id) {
  let selectedComment = await getComment(id);
  patchForm[0].value = selectedComment.player;
  patchForm[1].value = selectedComment.comment;
  selectedId = selectedComment.id;
  selectedGame = selectedComment.game;
}




// GET Comments ///
async function getComments() {
    commentsTable.innerHTML = '';

    const response = await fetch(`http://localhost:8090/api/comment/${selectedGame}`)
    const comments = await response.json();
    
    if (comments.length > 0) printComments(comments)
    else commentsTable.innerHTML = 'No comments found'
}

// GET Comment By ID ///
async function getComment(id) {
    const response = await fetch(`http://localhost:8090/api/comment/v2/${id}`)
    const comment = await response.json();

    return comment;
}


// POST Comment ///
async function addComment() {
    if(addForm[0].value === '' || addForm[1].value === '' || addForm[2].value === '') {
        return;
    }

    const comment = {
        player: addForm[0].value,
        game: addForm[1].value,
        comment: addForm[2].value
    }

    const response = await fetch('http://localhost:8090/api/comment', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(comment)
    });

    selectedGame = addForm[1].value;
    getComments();
}

// PATCH Comment ///
async function patchComment() {
    if(patchForm[0].value === '' || patchForm[1].value === ''  || selectedId === '') {
        return;
    }

    const comment = {
        player: patchForm[0].value,
        comment: patchForm[1].value,
    }

    const response = await fetch(`http://localhost:8090/api/comment/${selectedId}`, { 
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(comment)
    });

    getComments();
}
  

// DELETE Comment By ID ///
async function deleteComment(id) {
  const response = await fetch(`http://localhost:8090/api/comment/${id}`, {
      method: 'DELETE',
  });

  getComments();
}






function printComments(comments) {
    commentsTable.innerHTML = '';

    commentsTable.innerHTML += `
    <tr>
        <th>ID</th>
        <th>Player</th>
        <th>Game</th>
        <th>Comment</th>
        <th>Commented On</th>
    </tr>
    `;
    
    comments.forEach(comment => {
      let date = new Date(comment.commentedOn);
      comment.commentedOn = date.toLocaleDateString();
      commentsTable.innerHTML += `
        <tr onclick="selectComment(${comment.id})">
            <td>${comment.id}</td>
            <td>${comment.player}</td>
            <td>${comment.game}</td>
            <td>${comment.comment}</td>
            <td>${comment.commentedOn}</td>
            <td><button class='btn-delete' onclick="deleteComment(${comment.id})">Delete</button></td>
        </tr>
        `;
    });
}