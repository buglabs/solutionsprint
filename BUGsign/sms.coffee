express = require 'express'
app = express.createServer()

app.get '/sms/:message', (req, res) ->
  console.log 'OHAI: ' + req.params.message
  res.write req.params.message

app.listen 80
