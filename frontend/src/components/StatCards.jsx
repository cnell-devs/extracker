import { Stat } from "./subComponents/Stat";

export const StatCards = ({ data }) => {
  const income = data
    .filter((cat) => cat.personalFinanceCategory.primary == "INCOME")
    .map((transaction) => transaction.amount)
    .reduce((acc, curr) => curr + acc).toFixed(2);


  const expenses = data
    .filter((cat) => cat.personalFinanceCategory.primary != "INCOME")
    .map((transaction) => transaction.amount)
    .reduce((acc, curr) => curr + acc);
  const flow = (income - expenses).toFixed(2);

  console.log(data);

  return (
    <>
      <div className="stats shadow flex w-3/4">
        <Stat stat={{ title: "Income", value: income, desc: "Income desc" }} />
        <Stat
          stat={{ title: "Expenses", value: expenses, desc: "Expenses desc" }}
        />
        <Stat stat={{ title: "Cash Flow", value: flow, desc: "Flow desc" }} />
      </div>
    </>
  );
};
