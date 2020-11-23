import json
import os
import time

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
    def __init__(self, eval, num_blocks, max_width, max_height, blocks, placed):
        self.eval = eval
        self.num_blocks = num_blocks
        self.max_width = max_width
        self.max_height = max_height
        self.blocks = blocks
        self.placed = placed

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
            ax.text((block.x + block.size_x * 0.5) * proportion_x,
                    (block.y + block.size_y * 0.5) * proportion_y,
                    str(block.id),
                    horizontalalignment='center',
                    verticalalignment='center',
                    multialignment="center",
                    fontsize=max(min(block.size_x, block.size_y) * 0.5, 16),
                    color='b',
                    transform=ax.transAxes)

        # Creating the are
        ax.set_xlim(0, self.max_width)
        ax.set_ylim(0, self.max_height)

        # Remove ticks from axis
        ax.set_yticks([])
        ax.set_xticks([])


def plot_evolution_ea(data_path, first_sol, best_sol, name, desc, filepath, show=False):
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

    # Plot results
    fig, axs = plt.subplots(nrows=2, ncols=2, figsize=(8, 8))

    try:
        axs[0][0].plot(iterations, best_solutions_iter, label='Best (it.)')  # Plot more data on the axes...
        axs[0][0].plot(iterations, average_solutions_iter, label='Average (it.)')  # ... and some more.
        axs[0][0].plot(iterations, best_solutions, label='Best (global)')  # Plot some data on the axes.
        axs[0][0].set_xlabel('Iterations')  # Add an x-label to the axes.
        axs[0][0].set_ylabel('Placed blocks')  # Add a y-label to the axes.
        axs[0][0].set_title("Solutions")  # Add a title to the axes.
        axs[0][0].legend()  # Add a legend.
        y_ticks = np.arange(0, int(best_solutions[-1] * 1.2), best_solutions[-1] // 10)
        x_ticks = np.arange(0, int(iterations[-1] * 1.2), iterations[-1] // 5)

        axs[0][0].set_yticks(y_ticks)
        axs[0][0].set_xticks(x_ticks)
        axs[0][0].set_xlim(0, iterations[-1])
        axs[0][0].set_ylim(0, best_solutions[-1])

        # Plot times
        axs[1][0].stackplot(iterations, times)  # Plot more data on the axes...
        axs[1][0].set_xlabel('Iterations')  # Add an x-label to the axes.
        axs[1][0].set_ylabel('Since start (ms)')  # Add a y-label to the axes.
        axs[1][0].set_title("Times")  # Add a title to the axes.
        y_ticks = np.arange(0, times[-1], times[-1] // 8)
        axs[1][0].set_yticks(y_ticks)
        axs[1][0].set_xticks(x_ticks)
        axs[1][0].set_xlim(0, iterations[-1])
        axs[1][0].set_ylim(0, times[-1])

        first_sol.get_plot(axs[0][1])
        axs[0][1].set_title("First Solution")
        best_sol.get_plot(axs[1][1])
        axs[1][1].set_title("Best Solution")

    except ZeroDivisionError:
        print("ERROR: ZeroDivisionError for: " + name + "[" + description + "]")

    fig.suptitle(name)
    plt.figtext(0.5, 0.001, desc, wrap=True, ha='center', fontsize=10)
    fig.tight_layout()
    plt.savefig(filepath, format="svg")
    if show:
        plt.show()
    plt.close(fig)
    plt.cla()


def plot_evolution_sa(data_path, first_sol, best_sol, name, desc, filepath, show=False):
    iterations = []
    evaluations = []
    times = []
    best_solutions = []
    with open(data_path, "rt") as f:
        try:
            line = f.readline()
            while line:
                line_values = line.split("\t")
                iterations.append(int(line_values[0]))
                evaluations.append(int(line_values[1]))
                times.append(int(line_values[2]))
                best_solutions.append(int(line_values[3]))
                line = f.readline()
        except EOFError:
            print("EOF reached")

        # Plot results
        fig, axs = plt.subplots(nrows=2, ncols=2, figsize=(8, 8))

        try:
            axs[0][0].plot(iterations, best_solutions, label='Best')  # Plot some data on the axes.
            axs[0][0].set_xlabel('Iterations')  # Add an x-label to the axes.
            axs[0][0].set_ylabel('Placed blocks')  # Add a y-label to the axes.
            axs[0][0].set_title("Solutions")  # Add a title to the axes.
            axs[0][0].legend()  # Add a legend.
            y_ticks = np.arange(0, int(best_solutions[-1] * 1.2), best_solutions[-1] // 10)
            x_ticks = np.arange(0, int(iterations[-1] * 1.2), iterations[-1] // 5)
            axs[0][0].set_yticks(y_ticks)
            axs[0][0].set_xticks(x_ticks)
            axs[0][0].set_xlim(0, iterations[-1])
            axs[0][0].set_ylim(0, best_solutions[-1])

            # Plot times
            axs[1][0].stackplot(iterations, times)  # Plot more data on the axes...
            axs[1][0].set_xlabel('Iterations')  # Add an x-label to the axes.
            axs[1][0].set_ylabel('Since start (ms)')  # Add a y-label to the axes.
            axs[1][0].set_title("Times")  # Add a title to the axes.
            y_ticks = np.arange(0, times[-1], times[-1] // 8)
            axs[1][0].set_yticks(y_ticks)
            axs[1][0].set_xticks(x_ticks)
            axs[1][0].set_xlim(0, iterations[-1])
            axs[1][0].set_ylim(0, times[-1])

            first_sol.get_plot(axs[0][1])
            axs[0][1].set_title("First Solution")
            best_sol.get_plot(axs[1][1])
            axs[1][1].set_title("Best Solution")
        except ZeroDivisionError:
            print("ERROR: ZeroDivisionError for: " + name + "[" + description + "]")

        fig.suptitle(name)
        plt.figtext(0.5, 0.001, desc, wrap=True, horizontalalignment='center', fontsize=10)
        fig.tight_layout()
        plt.savefig(filepath, format="svg")
        if show:
            plt.show()
        plt.close(fig)


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
            id_count += 1
        new_solution = Solution(eval, num_blocks, max_width, max_height, sol_blocks, placed)
        solutions.append(new_solution)

    return solutions


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    ea_file_names = []
    for dirname, subdirs, files in os.walk("../res/evals/EA"):
        for filename in files:
            ea_file_names.append(filename)

    for file_name in ea_file_names:
        file_name_no_ext = file_name[0:file_name.index(".")]
        solutions = load_solutions_from_json("../res/reps/EA/" + file_name_no_ext + ".json")
        descriptors = file_name_no_ext.split("_")
        description = "Gens.:{} Init. Pop:{} Parents:{} C.R.:{}" \
                      " {} Children S.F.:{} Case:{}".format(descriptors[0], descriptors[1], descriptors[2],
                                                            descriptors[3], str(int(descriptors[4]) / 100),
                                                            descriptors[5], descriptors[0])
        plot_evolution_ea("../res/evals/EA/" + file_name, solutions[0], solutions[-1], "Evolutionary Algorithm",
                          description, "../res/graphs/EA/" + file_name_no_ext + ".svg")

    sa_file_names = []
    for dirname, subdirs, files in os.walk("../res/evals/SA"):
        for filename in files:
            sa_file_names.append(filename)

    for file_name in sa_file_names:
        file_name_no_ext = file_name[0:file_name.index(".")]
        solutions = load_solutions_from_json("../res/reps/SA/" + file_name_no_ext + ".json")
        descriptors = file_name_no_ext.split("_")
        description = "{} Iters C.F. Type:{} Init. Temp.:{} " \
                      "Fin. Temp.:{} Alpha:{} S.F.:{} Case:{}".format(descriptors[0],
                                                                      descriptors[1],
                                                                      descriptors[2],
                                                                      descriptors[3],
                                                                      str(int(
                                                                          descriptors[4]) / 100),
                                                                      descriptors[5],
                                                                      descriptors[0])
        plot_evolution_sa("../res/evals/SA/" + file_name, solutions[0], solutions[-1], "Simulated Annealing",
                          description, "../res/graphs/SA/" + file_name_no_ext + ".svg", )
