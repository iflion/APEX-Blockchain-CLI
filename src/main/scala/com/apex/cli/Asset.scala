package com.apex.cli

import com.apex.core.{Transaction, TransactionType}
import com.apex.crypto.{BinaryData, Ecdsa, Fixed8, UInt256}
import play.api.libs.json.{JsNull, Json}

class AssetCommand extends CompositeCommand {
  override val cmd: String = "asset"
  override val description: String = "Interface to operate your funds,  omit it and type the sub command directly is legal."

  override val subCommands: Seq[Command] = Seq(
    new CirculateCommand,
    new SendCommand
  )
}

class CirculateCommand extends SendCommand {
  override val cmd = "circulate"
  override val description = "Transfer tokens between accounts within current wallet. "

  override val paramList: ParameterList = ParameterList.create(
    new NicknameParameter("from", "from", true),
    new NicknameParameter("to", "to"),
    new AmountParameter("amount", "amount")
  )
}

class SendCommand extends Command {
  override val cmd = "send"
  override val description = "Transfer tokens."
  override val sys: Boolean = true

  override val paramList: ParameterList = ParameterList.create(
    new NicknameParameter("from", "from", true),
    new AddressParameter("to", "to"),
    new AmountParameter("amount", "amount")
  )

  override def execute(params: List[String]): Result = {

    val checkResult = Account.checkWalletStatus
    if(!checkResult.isEmpty) InvalidParams(checkResult)
    else {
      // 赋值from昵称
      var from = WalletCache.getActivityWallet().implyAccount
      // 根据昵称获取转账地址
      if(params.size/2 == paramList.params.size)  from = paramList.params(0).asInstanceOf[NicknameParameter].value

      // 赋值to收账地址
      var toAdress = ""
      if(this.cmd.equals("circulate")) {
        var to = paramList.params(1).asInstanceOf[NicknameParameter].value
        // 获取用户地址
        val toAccount = Account.getAccount(to)
        if (toAccount != null) toAdress = Account.getAccount(to).address
      }else toAdress = paramList.params(1).asInstanceOf[AddressParameter].value


      if(Account.getAccount(from) == null) InvalidParams("from account not exists, please type a different one")
      else if(toAdress.isEmpty) InvalidParams("to account not exists, please type a different one")
      else{
        val amount = paramList.params(2).asInstanceOf[AmountParameter].value
        val privKey = Account.getAccount(from).getPrivKey()

        /*val account = RPC.post("showaccount", s"""{"address":"${privKey.publicKey.address}"}""")

        var nextNonce: Long = 0
        if (account != JsNull) {
          nextNonce = (account \ "nextNonce").as[Long]
        }

        val tx = new Transaction(TransactionType.Transfer,
          privKey.publicKey,
          Ecdsa.PublicKeyHash.fromAddress(toAdress).get,
          "",
          Fixed8.fromDecimal(amount),
          UInt256.Zero,
          nextNonce,
          BinaryData.empty,
          BinaryData.empty)

        tx.sign(privKey)

        val txRawData = BinaryData(tx.toBytes)
        val rawTx: String = "{\"rawTx\":\""  + txRawData.toString  + "\"}"
        val result = RPC.post("sendrawtransaction", rawTx)

        Success(Json prettyPrint result)*/
        Success("send")
      }
    }
  }
}
