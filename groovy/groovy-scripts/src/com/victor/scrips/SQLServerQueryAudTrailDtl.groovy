import groovy.sql.*
import groovy.xml.XmlUtil

def dbHost	= 'jdbc:jtds:sqlserver://WPDCLDUT00156:1433/TGPOA01'
def dbUser	= 'commutil'
def dbPass	= 'C0mmutil123'
def dbDrvr	= 'net.sourceforge.jtds.jdbcx.JtdsDataSource'

def aSql = Sql.newInstance(dbHost, dbUser, dbPass, dbDrvr)
String aRecordId = this.args[0] ? this.args[0] : null
String aQuery = null

if (!aRecordId) return

if (aRecordId.isNumber()) {
	aQuery = "SELECT * FROM AUD_TRAIL_DTL WHERE AUD_TRAIL_REC_ID = :id"
}
else {
	aQuery = "SELECT * FROM AUD_TRAIL_DTL WHERE AUD_TRAIL_REC_ID IN " +
			"(SELECT TOP 1 AUD_TRAIL_REC_ID FROM AUD_TRAIL " + 
				"WHERE RCV_TMPLT_ID = :id " + 
				"ORDER BY JAVA_MESSAGING_SRVC_TS DESC)"
}

aSql.eachRow(aQuery, [id: aRecordId]) { aRow ->
	if (aRow.AUD_PRCS_STAGE_CD == 'CONT') {
        println "${aRow.AUD_PRCS_STAGE_CD} - ${aRow.LST_UPDT_TS}\n${aRow.AUD_TRAIL_MSG_TX.getBinaryStream()}\n"
    }
    else {
    	def aMsgInStr = aRow.AUD_TRAIL_MSG_TX.getBinaryStream()
		int aSize = aMsgInStr.available()
		byte[] byteArr = new byte[aSize]
		aMsgInStr.read(byteArr, 0, aSize)
		String aMsgStr = new String(byteArr)

		println "${aRow.AUD_PRCS_STAGE_CD} - ${aRow.LST_UPDT_TS}\n${XmlUtil.serialize(aMsgStr)}\n"
	}
}


