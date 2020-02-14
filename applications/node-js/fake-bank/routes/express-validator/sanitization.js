var express = require('express');
var router = express.Router();
const status = require('http-status')

const { sanitizeBody } = require('express-validator')

/* GET users listing. */
router.post('/', [
  sanitizeBody('UserName'),
  sanitizeBody('BankCode'),
  sanitizeBody('RequestId'),
  sanitizeBody('CardNumber')
], function(req, res, next) {
  let UserName = req.body.UserName
  let BankCode = req.body.BankCode
  let RequestId = req.body.RequestId
  let CardNumber = req.body.CardNumber

  let result = {
    UserName: UserName,
    BankCode: BankCode,
    RequestId: RequestId,
    CardNumber: CardNumber
  }

  return res.status(status.OK).json(result).end()
});

module.exports = router;
