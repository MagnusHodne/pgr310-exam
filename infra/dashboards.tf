resource "aws_cloudwatch_dashboard" "main" {
  dashboard_name = var.candidate_id
  dashboard_body = <<DASHBOARD
{
  "widgets": [
    {
      "type": "metric",
      "x": 0,
      "y": 0,
      "width": 12,
      "height": 6,
      "properties": {
        "metrics": [
          [
            "${var.candidate_id}",
            "carts.value"
          ]
        ],
        "period": 300,
        "stat": "Maximum",
        "region": "eu-west-1",
        "title": "Max number of carts per 5 minutes"
      }
    },
    {
      "type": "metric",
      "x": 12,
      "y": 0,
      "width": 12,
      "height": 6,
      "properties": {
        "metrics": [
          [
            "${var.candidate_id}",
            "cartsvalue.value"
          ]
        ],
        "period": 300,
        "stat": "Maximum",
        "region": "eu-west-1",
        "title": "Total sum of all carts per 5 minutes"
      }
    },
    {
      "type": "metric",
      "x": 0,
      "y": 6,
      "width": 12,
      "height": 6,
      "properties": {
        "metrics": [
          [
            "${var.candidate_id}",
            "checkouts.count"
          ]
        ],
        "period": 3600,
        "stat": "Maximum",
        "region": "eu-west-1",
        "title": "Total number of performed checkouts per hour"
      }
    },
    {
      "type": "metric",
      "x": 12,
      "y": 6,
      "width": 12,
      "height": 6,
      "properties": {
        "metrics": [
          [
            "${var.candidate_id}",
            "checkout_latency.avg"
          ]
        ],
        "period": 300,
        "stat": "Average",
        "region": "eu-west-1",
        "title": "Average latency of the checkout method"
      }
    }
  ]
}
DASHBOARD
}