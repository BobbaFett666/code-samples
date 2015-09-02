import groovy.sql.*
import groovy.xml.XmlUtil

def dbHost	= 'jdbc:db2://APDWD605.dev.ipc.us.aexp.com:51020/PVSS102D'
def dbUser	= 'commutil'
def dbPass	= 'commutil123'
def dbDrvr	= 'com.ibm.db2.jcc.DB2Driver'

def aSql = Sql.newInstance(dbHost, dbUser, dbPass, dbDrvr)
String aRecordId = this.args[0] ? this.args[0] : null
String aQuery = null

if (!aRecordId) return

if (aRecordId.isNumber()) {
	aQuery = "SELECT * FROM TGPOA01.AUD_TRAIL_DTL WHERE AUD_TRAIL_REC_ID = :id"
}
else {
	aQuery = "SELECT * FROM TGPOA01.AUD_TRAIL_DTL WHERE AUD_TRAIL_REC_ID IN " +
			"(SELECT AUD_TRAIL_REC_ID FROM TGPOA01.AUD_TRAIL " + 
				"WHERE RCV_TMPLT_ID = :id " + 
				"ORDER BY JAVA_MESSAGING_SRVC_TS DESC " +
				"FETCH FIRST 1 ROWS ONLY)"
}

aSql.eachRow(aQuery, [id: aRecordId]) { aRow ->

	try {
    	def aMsgInStr = aRow.AUD_TRAIL_MSG_TX.getBinaryStream()
		int aSize = aMsgInStr.available()
		byte[] byteArr = new byte[aSize]
		aMsgInStr.read(byteArr, 0, aSize)
		String aMsgStr = new String(byteArr)

		println "${aRow.AUD_PRCS_STAGE_CD} - ${aRow.LST_UPDT_TS}\n${XmlUtil.serialize(aMsgStr)}\n"

	}
	catch (Exception e) {
		println "${aRow.AUD_PRCS_STAGE_CD} - ${aRow.LST_UPDT_TS}\n${aRow.AUD_TRAIL_MSG_TX.getBinaryStream()}\n"
	}
}


