db.createUser({
	user: 'foodie',
	pwd: 'foodie',
	roles: [ 'readWrite' ]
});

db.createCollection('recipe');
db.getCollection('recipe').createIndex({
	title: 1,
});
db.getCollection('recipe').createIndex({
	"ingredients.name": 1,
});