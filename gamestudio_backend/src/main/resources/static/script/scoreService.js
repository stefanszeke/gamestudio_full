
const APIURL = 'http://localhost:8090/api';

const sendScore = async (player, game, points) => {
  try {

    const score = { player, game, points };

    const response = await fetch(`${APIURL}/score`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(score)
    });

    const data = await response.json();

    console.log(data);

  } catch (err) { console.log(err) }
}
