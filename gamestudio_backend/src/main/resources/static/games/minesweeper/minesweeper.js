console.log("Minesweeper JS loaded");

const gameBoard = document.querySelector(".minesweeper_board");

let board = [];
let bombs = 10;
let size = 10;

function init() {
  // Create game board
  for (let i = 0; i < 10; i++) {
    board.push([]);
    for (let j = 0; j < 10; j++) {
      board[i].push({ hidden: true, bomb: false, flagged: false, value: 0 });
    }
  }

  // Place bombs randomly
  let bombCount = 0;
  while (bombCount < bombs) {
    const row = Math.floor(Math.random() * 10);
    const col = Math.floor(Math.random() * 10);
    if (!board[row][col].bomb) {
      board[row][col].bomb = true;
      board[row][col].value = "💣";
      bombCount++;
    }
  }

  // place clues
  generateClues();
  refreshBoard();
}

init();


function generateClues() {
  for (let i = 0; i < size; i++) {
    for (let j = 0; j < size; j++) {
      if (board[i][j].value !== "💣") {
        board[i][j].value = countAdjacentMines(i, j);
      }
    }
  }
}

function countAdjacentMines(x, y) {
  let count = 0;
  for (let i = -1; i <= 1; i++) {
    let actRow = x + i;
    if (actRow >= 0 && actRow < size) {
      for (let j = -1; j <= 1; j++) {
        let actCol = y + j;
        if (actCol >= 0 && actCol < size) {
          if (board[actRow][actCol].bomb === true) {
            count++;
          }
        }
      }
    }
  }
  return count;
}



function refreshBoard() {
  gameBoard.innerHTML = "";
  for (let i = 0; i < size; i++) {
    for (let j = 0; j < size; j++) {
      const cell = document.createElement("div");
      cell.classList.add("cell");
      cell.dataset.row = i.toString();
      cell.dataset.col = j.toString();
      cell.addEventListener("click", () => openTile(i, j));
      // cell.addEventListener("contextmenu", handleRightClick);
      if (!board[i][j].hidden) {
        cell.innerHTML = board[i][j].value;
      }
      gameBoard.appendChild(cell);
    }
  }
}

function openTile(x, y) {

  if (board[x][y].hidden === true) {
    console.log(board[x][y]);
    board[x][y].hidden = false;
    if (board[x][y].value === 0) {
      openAdjacentTiles(x, y);
    }
  }

  if (board[x][y].value === '💣') {
    alert('Game Over');
  }

  refreshBoard();
}

function openAdjacentTiles(x, y) {
  for (let i = -1; i <= 1; i++) {
    let actRow = x + i;
    if (actRow >= 0 && actRow < size) {
      for (let j = -1; j <= 1; j++) {
        let actCol = y + j;
        if (actCol >= 0 && actCol < size) {
          if (board[actRow][actCol].hidden === true) {
            openTile(actRow, actCol);
          }
        }
      }
    }
  }
}

function reset() {
  board = [];
  init();
  refreshBoard();
}