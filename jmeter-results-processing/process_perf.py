import pandas as pd
import sys
import numpy as np

data: pd.DataFrame = pd.read_csv(sys.argv[1])

data = data[data['label'] != 'Registration'][data['label'] != 'Auth']

grouped_data = data.groupby('label')

grouped_counts = grouped_data['success'].count()
success_counts = data[data['success']].groupby('label')['success'].count()

print(f"Success rate:\n\n{success_counts / grouped_counts}\n\n")

time_grouped = grouped_data['elapsed']
time_stats = pd.DataFrame()
time_stats['mean'] = time_grouped.mean()
time_stats['median'] = time_grouped.median()
time_stats['max'] = time_grouped.max()

print(f"Time, ms:\n\n{time_stats}\n\n")

throughput = grouped_counts * 1000.0 / time_grouped.sum()

print(f"Throughput, req/sec:\n\n{throughput}\n\n")
