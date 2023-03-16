const commentsTable = document.getElementById('comments-table');
const gameSelect = document.getElementById('gameSelect');
const addForm = document.getElementById('addForm');
const testButton = document.querySelector('.test');

const getCommentsButton = document.getElementById('getCommentsButton');

let selectedGame = '';

gameSelect.addEventListener('change', (event) => {
    selectedGame = event.target.value;
});

getCommentsButton.addEventListener('click', (e) => {
    console.log("game: " + gameSelect.value);
    selectedGame = gameSelect.value;
    getComments();
});

async function getComments() {
    commentsTable.innerHTML = '';
    // const getComments = new XMLHttpRequest();

    // getComments.open('GET', `http://localhost:8090/api/comment/${selectedGame}`);
    // getComments.setRequestHeader('Content-Type', 'application/json');

    const response = await fetch(`http://localhost:8090/api/comment/${selectedGame}`)
    const comments = await response.json();
    
        console.log(comments);
        
        if (comments.length > 0) {
            printComments(comments);
        } else {
            commentsTable.innerHTML = 'No comments found';
        }
}

async function addComment() {
    if(addForm[0].value === '' || addForm[1].value === '' || addForm[2].value === '') {
        return;
    }

    // const postComment = new XMLHttpRequest();

    // postComment.open('POST', 'http://localhost:8090/api/comment');
    // postComment.setRequestHeader('Content-Type', 'application/json');

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

testButton.addEventListener('click', (e) => {
    console.log("game: " + addForm[0].value);
    console.log("game: " + addForm[1].value);
    console.log("game: " + addForm[2].value);
});

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
        commentsTable.innerHTML += `
        <tr>
        <td>${comment.id}</td>
        <td>${comment.player}</td>
        <td>${comment.game}</td>
        <td>${comment.comment}</td>
        <td>${comment.commentedOn}</td>
        </tr>
        `;
    });
}