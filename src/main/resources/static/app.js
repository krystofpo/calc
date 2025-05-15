document.getElementById('btn').addEventListener('click', () => {
    fetch('/api/hello')
        .then(res => res.json())
        .then(data => {
            document.getElementById('output').textContent = data.message;
        })
        .catch(err => {
            console.error(err);
            document.getElementById('output').textContent = 'Error fetching greeting.';
        });
});
