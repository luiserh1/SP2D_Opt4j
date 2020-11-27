import json
import os
import time
from math import inf

import matplotlib
import matplotlib.pyplot as plt
import numpy as np
import matplotlib.patches as patches
from matplotlib.collections import PatchCollection

matplotlib.use("TkAgg")


# 'GTK3Agg', 'GTK3Cairo', 'MacOSX', 'nbAgg', 'Qt4Agg', 'Qt4Cairo', 'Qt5Agg', 'Qt5Cairo', 'TkAgg', 'TkCairo'
# 'WebAgg', 'WX', 'WXAgg', 'WXCairo', 'agg', 'cairo', 'pdf', 'pgf', 'ps', 'svg', 'template'

class Block:
    def __init__(self, id, size_x, size_y, pos_x=-1, pos_y=-1):
        self.id = id
        self.x = pos_x
        self.y = pos_y
        self.size_x = size_x
        self.size_y = size_y


class Solution:
    def __init__(self, eval, num_blocks, max_width, max_height, blocks, placed, fig_prop):
        self.eval = eval
        self.num_blocks = num_blocks
        self.max_width = max_width
        self.max_height = max_height
        self.blocks = blocks
        self.placed = placed
        self.fig_prop = fig_prop

    def plot(self):
        # Create figure and axes
        fig = plt.figure()
        ax = fig.add_subplot(111)

        self.get_plot(ax)

        # Plot the result
        plt.show()

    def get_plot(self, ax):
        block_boxes = []
        for block in self.blocks:
            if block.x < 0:
                continue
            red = 0.8 / self.num_blocks * block.id
            green = 0.8 / self.num_blocks * block.id
            blue = 0.8 / self.num_blocks * block.id
            color = (red, green, blue, 1.0)
            rect = patches.Rectangle((block.x, block.y), block.size_x, block.size_y, facecolor=color,
                                     linewidth=1, edgecolor="b")
            ax.add_patch(rect)

            # Printing the ID as text
            proportion_x = 1 / self.max_width
            proportion_y = 1 / self.max_height
            f_size = 16
            if block.id > 99:
                f_size = 12
            elif block.id > 999:
                f_size = 8

            f_size *= self.fig_prop

            ax.text((block.x + block.size_x * 0.5) * proportion_x,
                    (block.y + block.size_y * 0.5) * proportion_y,
                    str(block.id),
                    horizontalalignment='center',
                    verticalalignment='center',
                    multialignment="center",
                    fontsize=f_size,
                    color='b',
                    transform=ax.transAxes)

        # Creating the are
        ax.set_xlim(0, self.max_width)
        ax.set_ylim(0, self.max_height)

        # Remove ticks from axis
        x_step = max(self.max_width // 10, 1)
        y_step = max(self.max_height // 10, 1)
        ax.set_xticks(list(range(0, self.max_width, x_step)) + [self.max_width])
        ax.set_yticks(list(range(0, self.max_height, y_step)) + [self.max_height])

        ax.grid(True, color=(0.0, 0.8, 0.0, 0.4), linestyle='--', linewidth=1)

        """par1 = ax.twinx()
        par2 = ax.twiny()
        par1.set_xticks(list(range(0, self.max_width, x_step)) + [self.max_width])
        par2.set_yticks(list(range(0, self.max_height, y_step)) + [self.max_height])"""


def plot_evolution_ea(data_path, first_sol, best_sol, name, desc, filepath, max_samples=inf, show=False):
    iterations = []
    evaluations = []
    times = []
    worst_solutions_iter = []
    best_solutions_iter = []
    average_solutions_iter = []
    best_solutions = []
    with open(data_path, "rt") as f:
        try:
            line = f.readline()
            while line:
                line_values = line.split("\t")
                iterations.append(int(line_values[0]))
                evaluations.append(int(line_values[1]))
                times.append(int(line_values[2]))
                worst_solutions_iter.append(int(line_values[3]))
                best_solutions_iter.append(int(line_values[4]))
                average_solutions_iter.append(float(line_values[5]))
                best_solutions.append(int(line_values[6]))
                line = f.readline()
        except EOFError:
            print("EOF reached")

    num_samples = iterations[-1]
    if num_samples > max_samples:
        steps = int(num_samples // max_samples)
        iterations = [iterations[i] for i in range(0, num_samples, steps)]
        evaluations = [evaluations[i] for i in range(0, num_samples, steps)]
        times = [times[i] for i in range(0, num_samples, steps)]
        worst_solutions_iter = [worst_solutions_iter[i] for i in range(0, num_samples, steps)]
        best_solutions_iter = [best_solutions_iter[i] for i in range(0, num_samples, steps)]
        average_solutions_iter = [average_solutions_iter[i] for i in range(0, num_samples, steps)]
        best_solutions = [best_solutions[i] for i in range(0, num_samples, steps)]

    # Plot results
    fig, axs = plt.subplots(nrows=2, ncols=2, figsize=(8, 8))

    axs[0][0].plot(evaluations, best_solutions_iter, label='Best (eval.)')  # Plot more data on the axes...
    axs[0][0].plot(evaluations, average_solutions_iter, label='Average (eval.)')  # ... and some more.
    axs[0][0].plot(evaluations, best_solutions, label='Best (global)')  # Plot some data on the axes.
    axs[0][0].set_xlabel('Evaluations')  # Add an x-label to the axes.
    axs[0][0].set_ylabel('Placed blocks')  # Add a y-label to the axes.
    axs[0][0].set_title("Solutions")  # Add a title to the axes.
    axs[0][0].legend()  # Add a legend.

    """y_ticks = np.arange(0, int(best_solutions[-1] * 1.2), best_solutions[-1] // 10)
    x_ticks = np.arange(0, int(evaluations[-1] * 1.2), evaluations[-1] // 5)
    axs[0][0].set_yticks(y_ticks)
    axs[0][0].set_xticks(x_ticks)
    axs[0][0].set_xlim(0, evaluations[-1])
    axs[0][0].set_ylim(0, best_solutions[-1])"""

    # Plot times
    axs[0][1].stackplot(evaluations, times)  # Plot more data on the axes...
    axs[0][1].set_xlabel('Evaluations')  # Add an x-label to the axes.
    axs[0][1].set_ylabel('Since start (ms)')  # Add a y-label to the axes.
    axs[0][1].set_title("Times")  # Add a title to the axes.
    y_ticks = np.arange(0, times[-1], times[-1] // 8)
    """axs[1][1].set_yticks(y_ticks)
    axs[1][1].set_xticks(x_ticks)"""
    axs[0][1].set_xlim(0, evaluations[-1])
    axs[0][1].set_ylim(0, times[-1])

    first_sol.get_plot(axs[1][0])
    axs[1][0].set_title("First Solution")
    best_sol.get_plot(axs[1][1])
    axs[1][1].set_title("Best Solution")

    fig.suptitle(name)
    plt.figtext(0.5, 0.001, desc, wrap=True, ha='center', fontsize=10)
    fig.tight_layout()
    plt.savefig(filepath, format="pdf")
    if show:
        plt.show()
    plt.close(fig)
    plt.cla()
    print("Saved: " + filepath)


def plot_evolution_sa(data_path, first_sol, best_sol, name, desc, filepath, max_samples=inf, show=False):
    iterations = []
    evaluations = []
    times = []
    best_solutions = []
    temperatures = []
    has_best_iter = False
    best_iter = []
    has_accepted = False
    accepted = []
    has_renewed = False
    renewed = []
    with open(data_path, "rt") as f:
        try:
            line = f.readline()
            while line:
                line_values = line.split("\t")
                iterations.append(int(line_values[0]))
                evaluations.append(int(line_values[1]))
                times.append(int(line_values[2]))
                best_solutions.append(int(line_values[3]))
                temperatures.append(float(line_values[4]))
                if line_values[5] != "\n":
                    best_iter.append(int(line_values[5]))
                    has_best_iter = True
                    if line_values[6] != "\n":
                        accepted.append(int(line_values[6]))
                        has_accepted = True
                        if line_values[7] != "\n":
                            renewed.append(int(line_values[6]))
                            has_renewed = True
                line = f.readline()
        except EOFError:
            print("EOF reached")

        if not iterations:
            print(filepath + ": Nothing to plot here")
            return

        num_samples = iterations[-1]
        if num_samples > max_samples:
            steps = int(num_samples // max_samples)
            iterations = [iterations[i] for i in range(0, num_samples, steps)]
            evaluations = [evaluations[i] for i in range(0, num_samples, steps)]
            times = [times[i] for i in range(0, num_samples, steps)]
            temperatures = [temperatures[i] for i in range(0, num_samples, steps)]
            best_solutions = [best_solutions[i] for i in range(0, num_samples, steps)]
            if has_best_iter:
                best_iter = [best_iter[i] for i in range(0, num_samples, steps)]
                if has_accepted:
                    accepted = [accepted[i] for i in range(0, num_samples, steps)]
                    if has_renewed:
                        renewed = [renewed[i] for i in range(0, num_samples, steps)]

        # Plot results
        fig, axs = plt.subplots(nrows=2, ncols=2, figsize=(8, 8))
        par1 = axs[0][0].twinx()

        try:
            axs[0][0].set_xlabel('Iterations')  # Add an x-label to the axes.
            axs[0][0].set_ylabel('Placed blocks')  # Add a y-label to the axes.
            axs[0][0].set_title("Solutions")  # Add a title to the axes.
            if has_best_iter:
                axs[0][0].plot(iterations, best_iter, "g", label='New sol.')  # Plot some data on the axes.
                if has_accepted:
                    axs[0][0].plot(iterations, accepted, "black", label='Accepted sol.')  # Plot some data on the axes.

            axs[0][0].spines['left'].set_color('blue')

            axs[0][0].plot(iterations, best_solutions, "b", label='Best sol.')  # Plot some data on the axes.

            axs[0][0].legend()

            par1.set_ylabel('Temperature Degree', rotation=270, color="red", rotation_mode='anchor')  # Add a y-label to the axes.
            p1, = par1.plot(iterations, temperatures, "r", label='Temp')  # Plot some data on the axes.
            par1.spines['right'].set_color('red')

            """y_ticks = np.arange(0, int(best_solutions[-1] * 1.2), best_solutions[-1] // 10)
            x_ticks = np.arange(0, int(iterations[-1] * 1.2), iterations[-1] // 5)
            axs[0][0].set_yticks(y_ticks)
            axs[0][0].set_xticks(x_ticks)
            axs[0][0].set_xlim(0, iterations[-1])
            axs[0][0].set_ylim(0, best_solutions[-1])"""

            # Plot times
            axs[0][1].stackplot(iterations, times)  # Plot more data on the axes...
            axs[0][1].set_xlabel('Iterations')  # Add an x-label to the axes.
            axs[0][1].set_ylabel('Since start (ms)')  # Add a y-label to the axes.
            axs[0][1].set_title("Times")  # Add a title to the axes.
            y_ticks = np.arange(0, times[-1], times[-1] // 8)
            """axs[0][1].set_yticks(y_ticks)
            axs[0][1].set_xticks(x_ticks)"""
            axs[0][1].set_xlim(0, iterations[-1])
            axs[0][1].set_ylim(0, times[-1])

            if has_renewed:
                par2 = axs[0][1].twinx()
                par2.set_ylabel('Renewed Sols.', rotation=270, color="orange",
                                rotation_mode='anchor')  # Add a y-label to the axes.
                p2, = par2.plot(iterations, renewed, "orange")  # Plot some data on the axes.
                par2.spines['right'].set_color('orange')

            first_sol.get_plot(axs[1][0])
            axs[1][0].set_title("First Solution")
            best_sol.get_plot(axs[1][1])
            axs[1][1].set_title("Best Solution")
        except ZeroDivisionError:
            print("ERROR: ZeroDivisionError for: " + name + "[" + filepath + "]")

        fig.suptitle(name)
        plt.figtext(0.5, 0.001, desc, wrap=True, horizontalalignment='center', fontsize=8)
        fig.tight_layout()
        plt.savefig(filepath, format="pdf")
        if show:
            plt.show()
        plt.close(fig)
        print("Saved: " + filepath)


def load_json(filename):
    with open(filename) as fh:
        obj = json.load(fh)
    return obj


def load_solutions_from_json(path):
    f = load_json(path)
    num_blocks = int(f["numBlocks"])
    max_width = int(f["maxWidth"])
    max_height = int(f["maxHeigth"])
    block_sizes = f["blockSizes"]
    evals = f["evals"]

    min_side = inf
    unplaced_blocks = []
    for block_dict in block_sizes:
        id = int(block_dict["id"])
        size_x = int(block_dict["sizeX"])
        size_y = int(block_dict["sizeY"])
        new_block = (id, size_x, size_y)
        unplaced_blocks.append(new_block)

    solutions = []
    for sol_dict in evals:
        eval = int(sol_dict["eval"])
        placed = int(sol_dict["best_sol"])
        reps = sol_dict["rep"]
        sol_blocks = []
        id_count = 0
        for block_pos in reps:
            sol_blocks.append(
                Block(unplaced_blocks[id_count][0],
                      unplaced_blocks[id_count][1],
                      unplaced_blocks[id_count][2],
                      int(block_pos["x"]),
                      int(block_pos["y"]))
            )

            min_block_side = min(sol_blocks[id_count].size_x, sol_blocks[id_count].size_y)
            if min_block_side and min_block_side < min_side:
                min_side = min_block_side

            id_count += 1
        new_solution = Solution(eval, num_blocks, max_width, max_height, sol_blocks, placed,
                                min_side / max(max_width, max_height) * 8)
        solutions.append(new_solution)

    return solutions


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    ea_graphs_filenames = set()
    for dirname, subdirs, files in os.walk("../res/graphs/EA"):
        for filename in files:
            ea_graphs_filenames.add(filename[0:filename.index(".")])

    ea_file_names = []
    for dirname, subdirs, files in os.walk("../res/evals/EA"):
        for filename in files:
            if filename[0:filename.index(".")] not in ea_graphs_filenames:
                ea_file_names.append(filename)

    for file_name in ea_file_names:
        file_name_no_ext = file_name[0:file_name.index(".")]
        solutions = load_solutions_from_json("../res/reps/EA/" + file_name_no_ext + ".json")
        descriptors = file_name_no_ext.split("_")
        description = "Gens.:{} Init. Pop:{} Parents:{} Children:{}" \
                      " C.R.:{} S.F.:{} Case:{}".format(descriptors[0], descriptors[1], descriptors[2],
                                                        descriptors[3], str(int(descriptors[4]) / 100),
                                                        descriptors[5], descriptors[6])
        if descriptors[6] == "RANDOM":
            description += "({}-{}x{}/{})".format(descriptors[7], descriptors[8], descriptors[9], descriptors[10])
        plot_evolution_ea("../res/evals/EA/" + file_name, solutions[0], solutions[-1], "Evolutionary Algorithm",
                          description, "../res/graphs/EA/" + file_name_no_ext + ".pdf", 500)

    sa_graphs_names = set()
    for dirname, subdirs, files in os.walk("../res/graphs/SA"):
        for filename in files:
            sa_graphs_names.add(filename[0:filename.index(".")])

    sa_file_names = []
    for dirname, subdirs, files in os.walk("../res/evals/SA"):
        for filename in files:
            if filename[0:filename.index(".")] not in sa_graphs_names:
                sa_file_names.append(filename)

    for file_name in sa_file_names:
        file_name_no_ext = file_name[0:file_name.index(".")]
        solutions = load_solutions_from_json("../res/reps/SA/" + file_name_no_ext + ".json")
        descriptors = file_name_no_ext.split("_")
        description = "{} Iters C.F. Type:{} Init. Temp.:{} " \
                      "Fin. Temp.:{} S.F.:{} Alpha:{:4} Case:{}".format(descriptors[0],
                                                                         descriptors[1],
                                                                         descriptors[2],
                                                                         descriptors[3],
                                                                         descriptors[5],
                                                                         ((float(descriptors[4]) / 100)
                                                                          if descriptors[1] == "EXPONENTIAL" else 0.0),
                                                                         descriptors[6])
        if descriptors[6] == "RANDOM":
            description += "({}-{}x{}/{})".format(descriptors[7], descriptors[8], descriptors[9], descriptors[10])
        plot_evolution_sa("../res/evals/SA/" + file_name, solutions[0], solutions[-1], "Simulated Annealing",
                          description, "../res/graphs/SA/" + file_name_no_ext + ".pdf", 500)
