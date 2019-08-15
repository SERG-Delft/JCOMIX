const puppeteer = require('puppeteer')

const url = 'http://localhost:8080/sbank/Affilier3DsecureForm.html'

let browser
let page

beforeAll(async () => {
	browser = await puppeteer.launch({})
	page = await browser.newPage()
})

describe('Software under test', () => {
	test('does not return test objective', async () => {
		let testObjective = '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:lu="http://commonws/cetrel/lu"> <soapenv:Header/> <soapenv:Body> <lu:perform> <lu:resInput> <lu:UserName>wgen0001</lu:UserName> <lu:IssuerBankCode>1 or/**/"a"="a" or</lu:IssuerBankCode> <lu:RequestId>0001User</lu:RequestId> <lu:CardNumber>123456789123456</lu:CardNumber> </lu:resInput> </lu:perform> </soapenv:Body></soapenv:Envelope>'

		await page.goto(url)

		await page.click('[name="CardNumber"]')
		await page.keyboard.type('123456789123456')
		await page.click('[name="BankCode"]')
		await page.keyboard.type('1 or/**/\"a\"=\"a\" or')
		await page.click('[name="UserName"]')
		await page.keyboard.type('wgen0001')
		await page.click('[name="RequestId"]')
		await page.keyboard.type('0001User')
		await page.click('[name="submit"]')
		await page.waitForSelector('pre')

		let result = await page.$eval('pre', e => e.innerText)
		result = result.replace(/\s\s+/g, ' ')

		expect(result).not.toBe(testObjective)
	})
})

afterAll(() => {
	browser.close()
})
