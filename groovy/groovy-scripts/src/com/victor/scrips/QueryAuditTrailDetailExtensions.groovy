import groovy.sql.*

def dbHost	= 'jdbc:jtds:sqlserver://WPDCLDUT00156:1433/TGPOA01'
def dbUser	= 'commutil'
def dbPass	= 'C0mmutil123'
def dbDrvr	= 'net.sourceforge.jtds.jdbcx.JtdsDataSource'

def aSql = Sql.newInstance(dbHost, dbUser, dbPass, dbDrvr)

def aRecordId = this.args[0] ? this.args[0] : 6560
String aQuery = "SELECT * FROM AUD_TRAIL_DTL_EXT WHERE AUD_TRAIL_REC_ID = :id"

aSql.eachRow(aQuery, [id: aRecordId]) { aRow ->
	if (aRow.AUD_PRCS_STAGE_CD == "CUST") 
		println "${aRow.AUD_TRAIL_MSG_XML_TX.getBinaryStream()}"
//	println "${aRow.AUD_TRAIL_REC_ID}\t${aRow.AUD_PRCS_STAGE_CD}\t${aRow.LST_UPDT_TS}"
}