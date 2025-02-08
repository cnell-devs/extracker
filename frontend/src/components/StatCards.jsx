import { Stat } from "./subComponents/Stat";

export const StatCards = ({ dataMap }) => {

    const income = dataMap.get("INCOME")
    const expenses = (dataMap.values().reduce((acc, curr) => acc + curr) - income).toFixed(2)
    const flow = (income - expenses).toFixed(2)



  return (
    <>
          <div className="stats shadow flex w-3/4">

              <Stat stat={{title: "Income", value: income, desc: "Income desc"}} />
              <Stat stat={{title: "Expenses", value: expenses, desc: "Expenses desc"}} />
              <Stat stat={{title: "Cash Flow", value: flow, desc: "Flow desc"}} />

      </div>
    </>
  );
};
