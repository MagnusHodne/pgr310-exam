resource "aws_cloudwatch_metric_alarm" "totalcarts" {
  alarm_name          = "customer-boom"
  namespace           = var.candidate_id
  metric_name         = "carts.value"

  comparison_operator = "GreaterThanThreshold"
  threshold           = "5"
  evaluation_periods  = "3"
  period              = "300"

  statistic           = "Maximum"

  alarm_description   = "Alarm goes off if max number of carts in the last 3 five minute periods exceeds 5"
  insufficient_data_actions = []
  alarm_actions = [aws_sns_topic.alarms.arn]
}

resource "aws_sns_topic" "alarms" {
  name = "alarm-topic-${var.candidate_id}"
}

resource "aws_sns_topic_subscription" "user_updates_sqs_target" {
  topic_arn = aws_sns_topic.alarms.arn
  protocol  = "email"
  endpoint  = var.candidate_email
}