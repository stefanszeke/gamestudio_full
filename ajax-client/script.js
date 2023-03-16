const commentsTable = document.getElementById('comments-table');
const gameSelect = document.getElementById('gameSelectInput');
const addForm = document.getElementById('addForm');
const testButton = document.querySelector('.test');

let selectedGame = '';

gameSelect.addEventListener('change', (event) => {
    selectedGame = event.target.value;
});

function getComments() {
    commentsTable.innerHTML = '';
    selectedGame = gameSelect.value;

    const getComments = new XMLHttpRequest();

    getComments.open('GET', `http://localhost:8090/api/comment/${selectedGame}`);
    getComments.setRequestHeader('Content-Type', 'application/json');
    
    getComments.onload = function() {
        const comments = JSON.parse(getComments.responseText);

        console.log(comments);
        
        if (comments.length > 0) {
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
        } else {
            commentsTable.innerHTML = 'No comments found';
        }
        
    }
    getComments.send();
}

function addComment() {
    if(addForm[0].value === '' || addForm[1].value === '' || addForm[2].value === '') {
        return;
    }

    const postComment = new XMLHttpRequest();

    postComment.open('POST', 'http://localhost:8090/api/comment');
    postComment.setRequestHeader('Content-Type', 'application/json');

    postComment.onload = function() {
        selectedGame = addForm[1].value;
        getComments();
    }

    const comment = {
        player: addForm[0].value,
        game: addForm[1].value,
        comment: addForm[2].value
    }

    postComment.send(JSON.stringify(comment));
}

testButton.addEventListener('click', (e) => {

    console.log("game: " + addForm[0].value);
});