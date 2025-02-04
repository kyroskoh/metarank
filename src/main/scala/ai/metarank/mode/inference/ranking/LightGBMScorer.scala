package ai.metarank.mode.inference.ranking
import io.github.metarank.ltrlib.booster.LightGBMBooster
import io.github.metarank.ltrlib.model.Query

case class LightGBMScorer(booster: LightGBMBooster) extends RankScorer {
  override def score(input: Query): Array[Double] = {
    val features = new Array[Double](input.rows * input.columns)
    var pos      = 0
    for {
      rowIndex <- 0 until input.rows
      row = input.getRow(rowIndex)
    } {
      System.arraycopy(row, 0, features, pos, row.length)
      pos += row.length
    }
    booster.predictMat(features, input.rows, input.columns)
  }
}

object LightGBMScorer {
  def apply(model: String): LightGBMScorer = LightGBMScorer(LightGBMBooster(model))
}
