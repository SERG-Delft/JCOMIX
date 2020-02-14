var express = require('express');
var router = express.Router();

const { check, validationResult } = require('express-validator')
const status = require('http-status')

/* GET users listing. */
router.post('/', [
  check('UserName', 'UserName cannot be blank').not().isEmpty(),
  check('BankCode', 'BankCode cannot be blank').not().isEmpty(),
  check('RequestId', 'RequestId cannot be blank').not().isEmpty(),
  check('CardNumber', 'CardNumber cannot be blank').not().isEmpty()
], function(req, res, next) {
  const errors = validationResult(req)
  if (!errors.isEmpty()) {
    return res.status(status.UNPROCESSABLE_ENTITY).json(errors.array()).end()
  }

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
