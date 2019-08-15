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
		let testObjective = '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:lu="http://commonws/cetrel/lu"> <soapenv:Header/> <soapenv:Body> <lu:perform> <lu:resInput> <lu:UserName>wgen0001</lu:UserName><!--</lu:UserName> <lu:IssuerBankCode>0111</lu:IssuerBankCode> <lu:RequestId>--><lu:IssuerBankCode>0 or~/**/1</lu:IssuerBankCode><lu:RequestId>0001User</lu:RequestId> <lu:CardNumber>123456789123456</lu:CardNumber> </lu:resInput> </lu:perform> </soapenv:Body></soapenv:Envelope>'

		await page.goto(url)

		await page.click('[name="CardNumber"]')
		await page.keyboard.type('2489125')
		await page.click('[name="BankCode"]')
		await page.keyboard.type('116')
		await page.click('[name="UserName"]')
		await page.keyboard.type('wgen0001a/mu:UseiNaNe><!0-')
		await page.click('[name="RequestId"]')
		await page.keyboard.type('8>Elu:Issfes:UnkCUge>0!lr~/**//</lu:IsnRer(aqkBode86licHequeqtIdB:.w3U=U')
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
