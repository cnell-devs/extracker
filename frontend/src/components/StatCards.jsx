import { Stat } from "./subComponents/Stat";

// ✅ Function to classify and sum transactions
const calculateStats = (transactions) => {
  if (!transactions || transactions.length === 0) {
    return { income: "0.00", expenses: "0.00", flow: "0.00" };
  }

  const income = transactions
    .filter(({ primaryCategory }) => primaryCategory === "INCOME")
    .reduce((acc, { amount }) => acc + Math.abs(amount), 0)
    .toFixed(2);

  const expenses = transactions
    .filter(({ primaryCategory }) => primaryCategory != "INCOME") // ✅ Identify expenses by positive values
    .reduce((acc, { amount }) => acc + amount, 0) // ✅ Sum all expense transactions
    .toFixed(2);

  const flow = (income - expenses).toFixed(2);

  return { income, expenses, flow };
};

export const StatCards = ({ data }) => {
  if (!data || data.length === 0) {
    return <p className="text-gray-500">Loading data...</p>; // ✅ Prevent rendering errors
  }

  // ✅ Calculate financial stats
  const { income, expenses, flow } = calculateStats(data);


  return (
    <div className="stats shadow flex w-3/4">
      <Stat
        stat={{ title: "Income", value: `${income}`, desc: "Total earnings" }}
      />
      <Stat
        stat={{
          title: "Expenses",
          value: `${expenses}`,
          desc: "Total spending",
        }}
      />
      <Stat
        stat={{ title: "Cash Flow", value: `${flow}`, desc: "Net balance" }}
      />
    </div>
  );
};
